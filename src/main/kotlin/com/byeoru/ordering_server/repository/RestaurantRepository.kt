package com.byeoru.ordering_server.repository

import com.byeoru.ordering_server.domain.FoodCategory
import com.byeoru.ordering_server.domain.Restaurant
import com.byeoru.ordering_server.domain.dto.response.RestaurantPreviewWithDistanceDto
import org.locationtech.jts.geom.Point
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.ArrayList
import javax.persistence.EntityManager
import javax.persistence.LockModeType

@Repository
class RestaurantRepository(private val em: EntityManager,
                           private val jdbcTemplate: JdbcTemplate) {

    fun save(restaurant: Restaurant) = em.persist(restaurant)

    fun findOne(id: Long): Restaurant = em.find(Restaurant::class.java, id)

    fun findOneWithOwner(id: Long): Restaurant {
        return em.createQuery(
            "select m from Restaurant m" +
                    " left join fetch m.owner where m.id =:id", Restaurant::class.java
        )
            .setParameter("id", id)
            .singleResult
    }

    fun findOneLock(id: Long): Restaurant {
        return em.find(Restaurant::class.java, id, LockModeType.OPTIMISTIC)
    }

    fun findAllWithRepresentativeMenu(
        customerLocation: Point,
        foodCategory: FoodCategory
    ): List<RestaurantPreviewWithDistanceDto> {
        val query = "select distinct * from(select * , " +
                "(ST_Distance_Sphere(ST_GeomFromText(?, 4326), location, 6373000)) as distance_meter " +
                "from restaurant order by distance_meter) result left join representative_menu " +
                "on result.restaurant_id = representative_menu.restaurant_id " +
                "where result.distance_meter <= 3000 and food_category=? order by result.distance_meter"
        return jdbcTemplate.query(query, ResultSetExtractor<List<RestaurantPreviewWithDistanceDto>> { rs: ResultSet ->
            val previews = HashMap<Long, RestaurantPreviewWithDistanceDto>()
            var preview: RestaurantPreviewWithDistanceDto?
            while (rs.next()) {
                val restaurantId = rs.getLong("restaurant_id")
                val restaurantName = rs.getString("restaurant_name")
                val profileImageUrl = rs.getString("profile_image_url")
                val backgroundImageUrl = rs.getString("background_image_url")
                val representativeFoodName = rs.getString("food_name")
                val distanceMeter = rs.getDouble("distance_meter").toInt()
                preview = previews[restaurantId]
                if (preview == null) {
                    preview = RestaurantPreviewWithDistanceDto(
                        distanceMeter, restaurantId, restaurantName, profileImageUrl, backgroundImageUrl
                    )
                    previews[restaurantId] = preview
                }
                if (representativeFoodName != null) {
                    preview.addRepresentativeFoodName(representativeFoodName)
                }
            }
            ArrayList(previews.values)
        }, customerLocation.toText(), foodCategory.toString())!!
    }

    fun findRestaurantRatingAverage(restaurantId: Long): Float {
        val avg = em.createQuery(
            "select coalesce(avg(m.rating), 0) from Review m" +
                    " where m.restaurant.id =:restaurantId", Double::class.java
        )
            .setParameter("restaurantId", restaurantId)
            .singleResult
        return avg.toFloat()
    }
}
package com.server.ordering.repository;

import com.server.ordering.domain.FoodCategory;
import com.server.ordering.domain.Restaurant;
import com.server.ordering.domain.Review;
import com.server.ordering.domain.dto.response.RestaurantPreviewWithDistanceDto;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class RestaurantRepository {

    private final EntityManager em;
    private final JdbcTemplate jdbcTemplate;

    public void save(Restaurant restaurant) {
        em.persist(restaurant);
    }

    public Restaurant findOne(Long id) {
        return em.find(Restaurant.class, id);
    }

    public Restaurant findOneWithOwner(Long id) {
        return em.createQuery("select m from Restaurant m left join fetch m.owner where m.id =:id", Restaurant.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public Restaurant findOneLock(Long id) {
        return em.find(Restaurant.class, id, LockModeType.OPTIMISTIC);
    }

    public List<RestaurantPreviewWithDistanceDto> findAllWithRepresentativeMenu(Point customerLocation, FoodCategory foodCategory) {

        String query = "select distinct * from(select * , " +
                "(ST_Distance_Sphere(ST_GeomFromText(?, 4326), location, 6373000)) as distance_meter " +
                "from restaurant order by distance_meter) result left join representative_menu " +
                "on result.restaurant_id = representative_menu.restaurant_id " +
                "where result.distance_meter <= 3000 and food_category=? order by result.distance_meter";

        return jdbcTemplate.query(query, (ResultSetExtractor<List<RestaurantPreviewWithDistanceDto>>) rs -> {
            HashMap<Long, RestaurantPreviewWithDistanceDto> previews = new HashMap<>();
            RestaurantPreviewWithDistanceDto preview;

            while (rs.next()) {
                Long restaurantId = rs.getLong("restaurant_id");
                String restaurantName = rs.getString("restaurant_name");
                String profileImageUrl = rs.getString("profile_image_url");
                String backgroundImageUrl = rs.getString("background_image_url");
                String representativeFoodName = rs.getString("food_name");
                int distanceMeter = (int) rs.getDouble("distance_meter");

                preview = previews.get(restaurantId);

                if (preview == null) {
                    preview = new RestaurantPreviewWithDistanceDto(restaurantId, restaurantName, profileImageUrl,
                            backgroundImageUrl, distanceMeter);
                    previews.put(restaurantId, preview);
                }

                if (representativeFoodName != null) {
                    preview.addRepresentativeFoodName(representativeFoodName);
                }
            }
            return new ArrayList<>(previews.values());
        }, customerLocation.toText(), foodCategory.toString());
    }



    public Restaurant findOneWithRepresentativeMenu(Long id) {
        return em.createQuery("select distinct m from Restaurant m left join fetch m.representativeMenus where m.id=:id", Restaurant.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public float findRestaurantRatingAverage(Long restaurantId) {
        double avg = em.createQuery("select coalesce(avg(m.rating), 0) from Review m where m.restaurant.id =:restaurantId", Double.class)
                .setParameter("restaurantId", restaurantId)
                .getSingleResult();
        return (float) avg;
    }
}

package com.byeoru.ordering_server.repository

import com.byeoru.ordering_server.domain.Order
import com.byeoru.ordering_server.domain.OrderStatus
import com.byeoru.ordering_server.domain.dto.response.RecentOrderRestaurantDto
import com.byeoru.ordering_server.domain.dto.response.SalesResponseDto
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.SQLException
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class OrderRepository(private val em: EntityManager,
                      private val jdbcTemplate: JdbcTemplate,
                      private val restaurantRepository: RestaurantRepository) {

    fun save(order: Order) {
        em.persist(order)
    }

    fun findOne(orderId: Long): Order {
        return em.find(Order::class.java, orderId)
    }

    fun findOneWithRestaurantWithOwner(orderId: Long): Order {
        return em.createQuery(
            "select m from Order m" +
                    " left join fetch m.restaurant r" +
                    " left join fetch r.owner where m.id =:orderId", Order::class.java
        )
            .setParameter("orderId", orderId)
            .singleResult
    }

    fun findOneWithRestWithOrderFoodsWithFood(orderId: Long): Order {
        return em.createQuery(
            "select distinct m from Order m" +
                    " left join fetch m.restaurant" +
                    " left join fetch m.orderFoods o" +
                    " left join fetch o.food where m.id =:orderId", Order::class.java
        )
            .setParameter("orderId", orderId)
            .singleResult
    }

    fun findOneWithCustomer(orderId: Long): Order {
        return em.createQuery(
            "select m from Order m" +
                    " left join fetch m.customer where m.id =:orderId", Order::class.java
        )
            .setParameter("orderId", orderId)
            .singleResult
    }

    fun findOneWithCustomerAndRestaurant(orderId: Long): Order {
        return em.createQuery(
            "select m from Order m" +
                    " left join fetch m.customer" +
                    " left join fetch m.restaurant where m.id =:id", Order::class.java
        )
            .setParameter("id", orderId)
            .singleResult
    }

    fun findOneWithReview(orderId: Long): Order {
        return em.createQuery(
            "select distinct m from Order m" +
                    " left join fetch m.review where m.id =:id", Order::class.java
        )
            .setParameter("id", orderId)
            .singleResult
    }

    fun getDailySales(restaurantId: Long, yearAndMonth: String): List<SalesResponseDto> {
        return jdbcTemplate.query(
            "select temp_month, ifnull(a.sales, 0) as sales from (" +
                    " select date_format(received_time, '%Y-%m-%d') as order_month, sum(total_price) sales from orders" +
                    " where restaurant_id=? and date_format(received_time, '%Y-%m')=? and status='COMPLETED'" +
                    " group by date_format(received_time, '%Y-%m-%d')) a right join (WITH RECURSIVE cte AS (" +
                    " SELECT date_format(?,'%Y-%m-%d') AS dt UNION ALL" +
                    " SELECT date_add(dt,INTERVAL 1 DAY) FROM cte WHERE date_format(dt, '%Y-%m')=?)" +
                    " SELECT date_format(dt, '%Y-%m-%d') as temp_month FROM cte WHERE date_format(dt, '%Y-%m')=?) b" +
                    " on a.order_month = b.temp_month", SalesMapper(),
            restaurantId, yearAndMonth, "$yearAndMonth-01", yearAndMonth, yearAndMonth
        )
    }

    fun getMonthlySales(restaurantId: Long, year: String): List<SalesResponseDto> {
        return jdbcTemplate.query(
            "select temp_month, ifnull(a.sales, 0) as sales from (" +
                    " select date_format(received_time, '%Y-%m') as order_month, sum(total_price) sales from orders" +
                    " where restaurant_id=? and date_format(received_time, '%Y')=? and status='COMPLETED'" +
                    " group by date_format(received_time, '%Y-%m')) a right join (WITH RECURSIVE cte AS (" +
                    " SELECT date_format(?,'%Y-%m-%d') AS dt UNION ALL" +
                    " SELECT date_add(dt,INTERVAL 1 MONTH) FROM cte WHERE dt < ?)" +
                    " SELECT date_format(dt, '%Y-%m') as temp_month FROM cte) b" +
                    " on a.order_month = b.temp_month", SalesMapper(), restaurantId, year, "$year-01-01", "$year-12"
        )
    }

    // 조회 데이터 DTO 매핑
    internal class SalesMapper : RowMapper<SalesResponseDto> {
        @Throws(SQLException::class)
        override fun mapRow(rs: ResultSet, rowNum: Int): SalesResponseDto {
            return SalesResponseDto(rs.getString("sales"))
        }
    }

    fun findAllOrderedChecked(restaurantId: Long): List<Order> {
        return em.createQuery(
            "select distinct m from Order m" +
                    " where m.restaurant.id =:restaurantId and (m.status =:ordered or m.status =:checked)" +
                    " order by m.id desc", Order::class.java
        )
            .setParameter("restaurantId", restaurantId)
            .setParameter("ordered", OrderStatus.ORDERED)
            .setParameter("checked", OrderStatus.CHECKED)
            .resultList
    }

    fun findAllCanceledCompleted(restaurantId: Long): List<Order> {
        return em.createQuery(
            "select distinct m from Order m" +
                    " where m.restaurant.id =:restaurantId and (m.status =:canceled or m.status =:completed)" +
                    " order by m.canceledOrCompletedTime desc", Order::class.java
        )
            .setParameter("restaurantId", restaurantId)
            .setParameter("canceled", OrderStatus.CANCELED)
            .setParameter("completed", OrderStatus.COMPLETED)
            .resultList
    }

    fun findAllOngoingWithRestWithOrderFoodsByCustomerId(customerId: Long): List<Order> {
        return em.createQuery(
            "select distinct m from Order m" +
                    " left join fetch m.review" +
                    " left join fetch m.restaurant" +
                    " left join fetch m.orderFoods odf" +
                    " left join fetch odf.food" +
                    " where m.customer.id =:customerId and (m.status =:ordered or m.status =:checked)",
            Order::class.java
        )
            .setParameter("customerId", customerId)
            .setParameter("ordered", OrderStatus.ORDERED)
            .setParameter("checked", OrderStatus.CHECKED)
            .resultList
    }

    fun findAllFinishedWithRestWithOrderFoodsByCustomerId(customerId: Long): List<Order> {
        return em.createQuery(
            "select distinct m from Order m" +
                    " left join fetch m.review" +
                    " left join fetch m.restaurant" +
                    " left join fetch m.orderFoods odf" +
                    " left join fetch odf.food" +
                    " where m.customer.id =:customerId and (m.status =:canceled or m.status =:completed)" +
                    " order by m.id desc", Order::class.java
        )
            .setParameter("customerId", customerId)
            .setParameter("canceled", OrderStatus.CANCELED)
            .setParameter("completed", OrderStatus.COMPLETED) //.setFirstResult(offset)
            //.setMaxResults(limit)
            .resultList
    }

    fun findRecentWithRestaurant(customerId: Long, requestNumber: Int): List<RecentOrderRestaurantDto> {
        return jdbcTemplate.query("select restaurant_name, profile_image_url, background_image_url, r.restaurant_id " +
                "as restaurant_id, ordering_waiting_time from orders o " +
                "left join restaurant r on o.restaurant_id = r.restaurant_id " +
                "where order_id in (select max(order_id) from orders where customer_id = ? group by restaurant_id) " +
                "order by order_id desc limit ? offset 0", { rs: ResultSet, rowNum: Int ->

            // DTO 매핑
            val restaurantId = rs.getLong("restaurant_id")
            val restaurantName = rs.getString("restaurant_name")
            val profileImageUrl = rs.getString("profile_image_url")
            val backgroundImageUrl = rs.getString("background_image_url")
            val orderingWaitingTime = rs.getInt("ordering_waiting_time")
            val average: Float = restaurantRepository.findRestaurantRatingAverage(rs.getLong("restaurant_id"))
            RecentOrderRestaurantDto(
                restaurantId, restaurantName,
                profileImageUrl, backgroundImageUrl, average, orderingWaitingTime
            )
        }, customerId, requestNumber
        )
    }

    fun putCustomerIdToNull(customerId: Long) {
        em.createQuery("update Order m set m.customer.id = null where m.customer.id =:customerId")
            .setParameter("customerId", customerId)
            .executeUpdate()
    }
}
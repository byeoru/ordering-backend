package com.server.ordering.repository;

import com.server.ordering.domain.Order;
import com.server.ordering.domain.OrderStatus;
import com.server.ordering.domain.dto.response.RecentOrderRestaurantDto;
import com.server.ordering.domain.dto.response.SalesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager em;
    private final RestaurantRepository restaurantRepository;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

    public Order findOneWithRestaurantWithOwner(Long orderId) {
        return em.createQuery("select m from Order m" +
                        " left join fetch m.restaurant r" +
                        " left join fetch r.owner where m.id =:orderId", Order.class)
                .setParameter("orderId", orderId)
                .getSingleResult();
    }

    public Order findOneWithRestWithOrderFoodsWithFood(Long orderId) {
        return em.createQuery("select distinct m from Order m" +
                " left join fetch m.restaurant" +
                " left join fetch m.orderFoods o" +
                " left join fetch o.food where m.id =:orderId", Order.class)
                .setParameter("orderId", orderId)
                .getSingleResult();
    }

    public Order findOneWithCustomer(Long orderId) {
        return em.createQuery("select m from Order m left join fetch m.customer where m.id =:orderId", Order.class)
                .setParameter("orderId", orderId)
                .getSingleResult();
    }

    public Order findOneWithCustomerAndRestaurant(Long orderId) {
        return em.createQuery("select m from Order m" +
                        " left join fetch m.customer" +
                        " left join fetch m.restaurant where m.id =:id", Order.class)
                .setParameter("id", orderId)
                .getSingleResult();
    }

    public Order findOneWithReview(Long orderId) {
        return em.createQuery("select distinct m from Order m left join fetch m.review where m.id =:id", Order.class)
                .setParameter("id", orderId)
                .getSingleResult();
    }

    public List<SalesResponseDto> getDailySales(Long restaurantId, String yearAndMonth) {
        return jdbcTemplate.query("select temp_month, ifnull(a.sales, 0) as sales from (" +
                        " select date_format(received_time, '%Y-%m-%d') as order_month, sum(total_price) sales from orders" +
                        " where restaurant_id=? and date_format(received_time, '%Y-%m')=? and status='COMPLETED'" +
                        " group by date_format(received_time, '%Y-%m-%d')) a right join (WITH RECURSIVE cte  AS (" +
                        " SELECT date_format(?,'%Y-%m-%d') AS dt UNION ALL" +
                        " SELECT date_add(dt,INTERVAL 1 DAY) FROM cte WHERE date_format(dt, '%Y-%m')=?)" +
                        " SELECT date_format(dt, '%Y-%m-%d') as temp_month FROM cte WHERE date_format(dt, '%Y-%m')=?) b" +
                        " on a.order_month = b.temp_month", new SalesMapper(),
                restaurantId, yearAndMonth, yearAndMonth + "-01", yearAndMonth, yearAndMonth);
    }

    public List<SalesResponseDto> getMonthlySales(Long restaurantId, String year) {
        return jdbcTemplate.query("select temp_month, ifnull(a.sales, 0) as sales from (" +
                " select date_format(received_time, '%Y-%m') as order_month, sum(total_price) sales from orders" +
                " where restaurant_id=? and date_format(received_time, '%Y')=? and status='COMPLETED'" +
                " group by date_format(received_time, '%Y-%m')) a right join (WITH RECURSIVE cte  AS (" +
                " SELECT date_format('2022-01-01','%Y-%m-%d') AS dt UNION ALL" +
                " SELECT date_add(dt,INTERVAL 1 MONTH) FROM cte WHERE dt < ?)" +
                " SELECT date_format(dt, '%Y-%m') as temp_month FROM cte) b" +
                " on a.order_month = b.temp_month",new SalesMapper() , restaurantId, year, year + "-12");
    }

    // 조회 데이터 DTO 매핑
    static class SalesMapper implements RowMapper<SalesResponseDto> {
        @Override
        public SalesResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SalesResponseDto(rs.getString("sales"));
        }
    }

    public List<Order> findAllOrderedChecked(Long restaurantId) {
        return em.createQuery("select distinct m from Order m" +
                        " where m.restaurant.id =:restaurantId and (m.status =:ordered or m.status =:checked) order by m.id desc", Order.class)
                .setParameter("restaurantId", restaurantId)
                .setParameter("ordered", OrderStatus.ORDERED)
                .setParameter("checked", OrderStatus.CHECKED)
                .getResultList();
    }

    public List<Order> findAllCanceledCompleted(Long restaurantId) {
        return em.createQuery("select distinct m from Order m" +
                        " where m.restaurant.id =:restaurantId and (m.status =:canceled or m.status =:completed) order by m.canceledOrCompletedTime desc", Order.class)
                .setParameter("restaurantId", restaurantId)
                .setParameter("canceled", OrderStatus.CANCELED)
                .setParameter("completed", OrderStatus.COMPLETED)
                .getResultList();
    }

    public List<Order> findAllOngoingWithRestWithOrderFoodsByCustomerId(Long customerId) {
        return em.createQuery("select distinct m from Order m" +
                        " left join fetch m.review" +
                        " left join fetch m.restaurant" +
                        " left join fetch m.orderFoods odf" +
                        " left join fetch odf.food" +
                        " where m.customer.id =:customerId and (m.status =:ordered or m.status =:checked)", Order.class)
                .setParameter("customerId", customerId)
                .setParameter("ordered", OrderStatus.ORDERED)
                .setParameter("checked", OrderStatus.CHECKED)
                .getResultList();
    }

    public List<Order> findAllFinishedWithRestWithOrderFoodsByCustomerId(Long customerId) {
        return em.createQuery("select distinct m from Order m" +
                        " left join fetch m.review" +
                        " left join fetch m.restaurant" +
                        " left join fetch m.orderFoods odf" +
                        " left join fetch odf.food" +
                        " where m.customer.id =:customerId and (m.status =:canceled or m.status =:completed) order by m.receivedTime desc", Order.class)
                .setParameter("customerId", customerId)
                .setParameter("canceled", OrderStatus.CANCELED)
                .setParameter("completed", OrderStatus.COMPLETED)
                //.setFirstResult(offset)
                //.setMaxResults(limit)
                .getResultList();
    }

    public List<RecentOrderRestaurantDto> findRecentWithRestaurant(Long customerId, int requestNumber) {

        return jdbcTemplate.query("select restaurant_name, background_image_url, r.restaurant_id as restaurant_id, ordering_waiting_time from orders o " +
                "left join restaurant r on o.restaurant_id = r.restaurant_id " +
                "where order_id in (select max(order_id) from orders where customer_id = ? group by restaurant_id) " +
                "order by order_id desc limit ? offset 0", (rs, rowNum) -> {

            String restaurant_name = rs.getString("restaurant_name");
            String background_image_url = rs.getString("background_image_url");
            int ordering_waiting_time = rs.getInt("ordering_waiting_time");
            float average = restaurantRepository.findRestaurantRatingAverage(rs.getLong("restaurant_id"));

            return new RecentOrderRestaurantDto(restaurant_name, background_image_url, average, ordering_waiting_time);
        }, customerId, requestNumber);
    }

    public void putCustomerIdToNull(Long customerId) {
        em.createQuery("update Order m set m.customer.id = null where m.customer.id =:customerId")
                .setParameter("customerId", customerId)
                .executeUpdate();
    }
}
package com.server.ordering.repository;

import com.server.ordering.domain.Order;
import com.server.ordering.domain.dto.response.DailySalesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

//    public List<Map<String, Object>> getMonthlySales(Long restaurantId, String yearAndMonth) {
//        return jdbcTemplate.queryForList("select date_format(order_date, '%m/%d') as order_date, IFNULL(sum(total_price), 0) " +
//                "as daily_sales from orders where restaurant_id=? and date_format(order_date, '%Y/%m')=? " +
//                "group by date_format(order_date, '%m/%d')", restaurantId, yearAndMonth);
//    }

    public List<DailySalesDto> getMonthlySales(Long restaurantId, String from, String before) {
        return jdbcTemplate.query("select IFNULL(a.sales, 0) as dailySales from (select date_format(order_date, '%Y-%m-%d') as order_date, sum(total_price) sales from orders  where restaurant_id=? and date_format(order_date, '%Y-%m')=? group by date_format(order_date, '%Y-%m-%d')) a right join (select selected_date from (select adddate('2010-01-01',t4.i*10000 + t3.i*1000 + t2.i*100 + t1.i*10 + t0.i) selected_date from (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0, (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1, (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2, (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3, (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) v" +
                " where selected_date between ? and ? order by selected_date asc limit 31) b on a.order_date = b.selected_date", new SalesMapper(), restaurantId, from, from, before);
    }

    static class SalesMapper implements RowMapper<DailySalesDto> {
        @Override
        public DailySalesDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DailySalesDto(rs.getString("dailySales"));
        }
    }
}
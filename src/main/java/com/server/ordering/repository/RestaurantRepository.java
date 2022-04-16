package com.server.ordering.repository;

import com.server.ordering.domain.Restaurant;
import com.server.ordering.domain.dto.request.RestaurantInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

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

    public List<Restaurant> findAllWithRepresentativeMenu() {
        return em.createQuery("select m from Restaurant m left join fetch m.representativeMenus", Restaurant.class)
                .getResultList();
    }

    public Restaurant findOneWithRepresentativeMenu(Long id) {
        return em.createQuery("select m from Restaurant m left join fetch m.representativeMenus where m.id=:id", Restaurant.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public void put(String restaurantName, String ownerName, String address, int tableCount, String foodCategory,
                    String restaurantType, Long restaurantId) {
        jdbcTemplate.update("update restaurant set restaurant_name=?, owner_name=?, restaurant_address=?, table_count=?, " +
                "food_category=?, restaurant_type=? where restaurant_id=?", restaurantName,
                ownerName, address, tableCount, foodCategory,
                restaurantType, restaurantId);
    }
}

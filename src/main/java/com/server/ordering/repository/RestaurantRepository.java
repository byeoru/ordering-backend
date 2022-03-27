package com.server.ordering.repository;

import com.server.ordering.domain.Restaurant;
import com.server.ordering.domain.dto.request.RestaurantInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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

    public void put(Long restaurantId, RestaurantInfoDto dto) {
        jdbcTemplate.update("update restaurant set restaurant_name=?, owner_name=?, restaurant_address=?, table_count=?, " +
                "food_category=?, restaurant_type=? where restaurant_id=?", dto.getRestaurantName(),
                dto.getOwnerName(), dto.getAddress(), dto.getTableCount(), dto.getFoodCategory().toString(),
                dto.getRestaurantType().toString(), restaurantId);
    }
}

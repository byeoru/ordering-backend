package com.server.ordering.repository;

import com.server.ordering.domain.RepresentativeMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class RepresentativeMenuRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager em;

    public void save(Long restaurantId, Long foodId) {
        jdbcTemplate.update("insert into representative_menu (restaurant_id, food_id) values (?, ?)", restaurantId, foodId);
    }

    public void save(RepresentativeMenu representativeMenu) {
        em.persist(representativeMenu);
    }

    public void remove(String id) {
        jdbcTemplate.update("delete from representative_menu where id=?", id);
    }
}

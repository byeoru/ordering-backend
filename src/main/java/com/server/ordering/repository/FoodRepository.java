package com.server.ordering.repository;

import com.server.ordering.domain.Food;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FoodRepository {

    private final EntityManager em;
    private final JdbcTemplate jdbcTemplate;

    public void save(Food food) {
        em.persist(food);
    }

    public Food findOne(Long id) {
        return em.find(Food.class, id);
    }

    public List<Food> findAll(Long restaurantId) {
        return em.createQuery("select m from Food m where m.restaurant.id = :id", Food.class)
                .setParameter("id", restaurantId)
                .getResultList();
    }

    public void putFood(Long foodId, String foodName, int foodPrice, boolean soldOut, String imageUrl, String menuIntro) {
        jdbcTemplate.update("update food set food_name=?, food_price=?, sold_out=?, image_url=?, menu_intro=? where food_id",
                foodName, foodPrice, soldOut, imageUrl, menuIntro, foodId);
    }

    public void putFood(Long foodId, String foodName, int foodPrice, boolean soldOut, String menuIntro) {
        jdbcTemplate.update("update food set food_name=?, food_price=?, sold_out=?, menu_intro=? where food_id",
                foodName, foodPrice, soldOut, menuIntro, foodId);
    }

    public void remove(Food food) throws PersistenceException {
        em.remove(food);
    }

    public void remove(Long foodId) {
        jdbcTemplate.update("delete from food where food_id=?", foodId);
    }
}

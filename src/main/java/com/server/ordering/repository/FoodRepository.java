package com.server.ordering.repository;

import com.server.ordering.domain.Food;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FoodRepository {

    private final EntityManager em;

    public void save(Food food) {
        em.persist(food);
    }

    public List<Food> findAll(Long restaurantId) {
        return em.createQuery("select m from Food m where m.restaurant.id = :id", Food.class)
                .setParameter("id", restaurantId)
                .getResultList();
    }
}

package com.server.ordering.repository;

import com.server.ordering.domain.Basket;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class BasketRepository {

    private final EntityManager em;
    private final JdbcTemplate jdbcTemplate;

    public void save(Basket basket) {
        em.persist(basket);
    }

    public void save(Long customerId, Long foodId, int price, int count) {
        jdbcTemplate.update("insert into basket (customer_id, food_id, price, count) values (?,?,?,?)",
                customerId, foodId, price, count);
    }

    public void remove(Long basketId) {
        Basket basket = em.find(Basket.class, basketId);
        em.remove(basket);
    }

    public void removeAll(Long customerId) {
        em.createQuery("delete from Basket m where m.customer.id =:id")
                .setParameter("id", customerId)
                .executeUpdate();
    }
}

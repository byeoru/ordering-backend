package com.server.ordering.repository;

import com.server.ordering.domain.Basket;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BasketRepository {

    private final EntityManager em;

    public void save(Basket basket) {
        em.persist(basket);
    }

    public List<Basket> findAllWithFoodByCustomerId(Long customerId) {
        return em.createQuery("select m from Basket m left join fetch m.food where m.customer.id = :customerId", Basket.class)
                .setParameter("customerId", customerId)
                .getResultList();
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

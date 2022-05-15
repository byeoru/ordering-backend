package com.server.ordering.repository;

import com.server.ordering.domain.Waiting;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class WaitingRepository {

    private final EntityManager em;
    private final JdbcTemplate jdbcTemplate;

    public void save(Waiting waiting) {
        em.persist(waiting);
    }

    public void remove(Long id) {
        Waiting waiting = em.find(Waiting.class, id);
        em.remove(waiting);
    }

    public Waiting findOne(Long customerId) {
        return em.createQuery("select m from Waiting m where m.customer.id = :customerId", Waiting.class)
                .setParameter("customerId", customerId)
                .getSingleResult();
    }

    public Waiting findOneWithRestaurant(Long customerId) {
        return em.createQuery("select m from Waiting m left join fetch m.restaurant where m.customer.id = :customerId", Waiting.class)
                .setParameter("customerId", customerId)
                .getSingleResult();
    }

    public Long getCountInFrontOfMe(Integer myWaitingNum, Long restaurantId) {
        Map<String, Object> result = jdbcTemplate.queryForMap("select count(*) as in_front_of_me from (select * from waiting where restaurant_id = ?) f where f.my_waiting_number < ?", restaurantId, myWaitingNum);
        return (Long) result.get("in_front_of_me");
    }
}

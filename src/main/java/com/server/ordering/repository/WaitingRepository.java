package com.server.ordering.repository;

import com.server.ordering.domain.Waiting;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;
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
        return em.createQuery("select m from Waiting m where m.customer.id =:customerId", Waiting.class)
                .setParameter("customerId", customerId)
                .getSingleResult();
    }

    public List<Waiting> findAllWithPhoneNumberByRestaurantId(Long restaurantId) {
        return em.createQuery("select distinct m from Waiting m left join fetch m.phoneNumber where m.restaurant.id =:restaurantId", Waiting.class)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
    }

    public Waiting findOneWithRestaurant(Long customerId) throws PersistenceException {
        return em.createQuery("select m from Waiting m left join fetch m.restaurant where m.customer.id =:customerId", Waiting.class)
                .setParameter("customerId", customerId)
                .getSingleResult();
    }

    public long getCountInFrontOfMe(Integer myWaitingNum, Long restaurantId) {
        Map<String, Object> result = jdbcTemplate.queryForMap("select count(*) as in_front_of_me from " +
                "(select * from waiting where restaurant_id = ?) f where f.my_waiting_number < ?", restaurantId, myWaitingNum);
        return (long) result.get("in_front_of_me");
    }
}

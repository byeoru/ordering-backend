package com.server.ordering.repository;

import com.server.ordering.domain.MyCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Repository
@RequiredArgsConstructor
public class MyCouponRepository {

    private final EntityManager em;

    public void save(MyCoupon myCoupon) {
        em.persist(myCoupon);
    }

    public void findOneWithCustomer(String serialNumber, Long customerId) throws NoResultException {
        em.createQuery("select m from MyCoupon m where m.serialNumber =:serialNumber and m.customer.id =:customerId", MyCoupon.class)
                .setParameter("serialNumber", serialNumber)
                .setParameter("customerId", customerId)
                .getSingleResult();
    }

    public void remove(String serialNumber, Long customerId) {
        MyCoupon myCoupon = em.createQuery("select m from MyCoupon m where m.serialNumber =: serialNumber and m.customer.id =: customerId", MyCoupon.class)
                .setParameter("serialNumber", serialNumber)
                .setParameter("customerId", customerId)
                .getSingleResult();
        em.remove(myCoupon);
    }
}

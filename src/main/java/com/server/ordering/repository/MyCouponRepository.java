package com.server.ordering.repository;

import com.server.ordering.domain.MyCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

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

    public List<MyCoupon> findAll(Long customerId) {
        return em.createQuery("select distinct m from MyCoupon m where m.customer.id =:customerId", MyCoupon.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }

    public void remove(Long couponId) {
        MyCoupon myCoupon = em.find(MyCoupon.class, couponId);
        em.remove(myCoupon);
    }
}

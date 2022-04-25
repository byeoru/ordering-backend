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
    private final JdbcTemplate jdbcTemplate;

    public void save(MyCoupon myCoupon) {
        em.persist(myCoupon);
    }

    public MyCoupon findOne(String serialNumber) throws NoResultException {
        return em.createQuery("select m from MyCoupon m where m.serialNumber=:serialNumber", MyCoupon.class)
                .setParameter("serialNumber", serialNumber)
                .getSingleResult();
    }

    public void remove(String serialNumber, Long customerId) {
        jdbcTemplate.update("delete from my_coupon where serial_number=? and customer_id=?", serialNumber, customerId);
    }
}

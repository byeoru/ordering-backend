package com.server.ordering.repository;

import com.server.ordering.domain.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponRepository {

    private final EntityManager em;

    public void save(Coupon coupon) {
        em.persist(coupon);
    }

    public Coupon find(String id) {
        return em.find(Coupon.class, id);
    }
}

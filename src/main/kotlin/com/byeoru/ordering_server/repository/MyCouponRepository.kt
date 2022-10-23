package com.byeoru.ordering_server.repository

import com.byeoru.ordering_server.domain.MyCoupon
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.NoResultException

@Repository
class MyCouponRepository(private val em: EntityManager) {

    fun save(myCoupon: MyCoupon) {
        em.persist(myCoupon)
    }

    @Throws(NoResultException::class)
    fun findOneWithCustomer(serialNumber: String?, customerId: Long?) {
        em.createQuery(
            "select m from MyCoupon m where m.serialNumber =:serialNumber and m.customer.id =:customerId",
            MyCoupon::class.java
        )
            .setParameter("serialNumber", serialNumber)
            .setParameter("customerId", customerId)
            .singleResult
    }

    fun findAll(customerId: Long): List<MyCoupon> {
        return em.createQuery(
            "select distinct m from MyCoupon m where m.customer.id =:customerId",
            MyCoupon::class.java
        )
            .setParameter("customerId", customerId)
            .resultList
    }

    fun remove(couponId: Long) {
        val myCoupon = em.find(MyCoupon::class.java, couponId)
        em.remove(myCoupon)
    }
}
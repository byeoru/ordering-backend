package com.byeoru.ordering_server.repository

import com.byeoru.ordering_server.domain.Coupon
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.NoResultException

@Repository
class CouponRepository(private val em: EntityManager) {

    fun save(coupon: Coupon) = em.persist(coupon)

    @Throws(NoResultException::class)
    fun find(id: String): Coupon = em.find(Coupon::class.java, id)
}
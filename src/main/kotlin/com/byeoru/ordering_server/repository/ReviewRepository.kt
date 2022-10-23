package com.byeoru.ordering_server.repository

import com.byeoru.ordering_server.domain.Review
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class ReviewRepository(private val em: EntityManager) {

    fun save(review: Review) = em.persist(review)

    fun findOne(reviewId: Long): Review = em.find(Review::class.java, reviewId)

    fun findAllWithOrderWithCustomerByRestaurantId(restaurantId: Long): List<Review> {
        return em.createQuery(
            "select distinct m from Review m" +
                    " left join fetch m.order o" +
                    " left join fetch o.customer where m.restaurant.id =:restaurantId" +
                    " order by m.id desc ", Review::class.java
        )
            .setParameter("restaurantId", restaurantId)
            .resultList
    }

    fun remove(reviewId: Long) {
        val review = em.find(Review::class.java, reviewId)
        em.remove(review)
    }
}
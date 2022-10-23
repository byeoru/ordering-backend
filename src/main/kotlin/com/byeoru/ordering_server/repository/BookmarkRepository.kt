package com.byeoru.ordering_server.repository

import com.byeoru.ordering_server.domain.Bookmark
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceException

@Repository
class BookmarkRepository(private val em: EntityManager) {

    fun save(bookmark: Bookmark) {
        em.persist(bookmark)
    }

    fun remove(bookmarkId: Long) {
        val bookmark = em.find(Bookmark::class.java, bookmarkId)
        em.remove(bookmark)
    }

    @Throws(PersistenceException::class)
    fun findOneByCustomerIdAndRestaurantId(customerId: Long, restaurantId: Long) {
        em.createQuery(
            "select m from Bookmark m where m.customer.id =:customerId and m.restaurant.id =:restaurantId",
            Bookmark::class.java
        )
            .setParameter("customerId", customerId)
            .setParameter("restaurantId", restaurantId)
            .singleResult
    }

    fun findAllWithRestWithRepresentativeByCustomerId(customerId: Long): List<Bookmark> {
        return em.createQuery(
            "select distinct m from Bookmark m" +
                    " left join fetch m.restaurant r" +
                    " left join fetch r.representativeMenus where m.customer.id =:customerId", Bookmark::class.java
        )
            .setParameter("customerId", customerId)
            .resultList
    }
}
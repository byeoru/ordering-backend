package com.byeoru.ordering_server.repository

import com.byeoru.ordering_server.domain.Waiting
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceException

@Repository
class WaitingRepository(private val em: EntityManager,
                        private val jdbcTemplate: JdbcTemplate) {

    fun save(waiting: Waiting) = em.persist(waiting)

    fun remove(id: Long) {
        val waiting = em.find(Waiting::class.java, id)
        em.remove(waiting)
    }

    fun findOne(customerId: Long): Waiting {
        return em.createQuery("select m from Waiting m where m.customer.id =:customerId", Waiting::class.java)
            .setParameter("customerId", customerId)
            .singleResult
    }

    fun findAllWithPhoneNumberByRestaurantId(restaurantId: Long): List<Waiting> {
        return em.createQuery(
            "select distinct m from Waiting m left join fetch m.phoneNumber where m.restaurant.id =:restaurantId",
            Waiting::class.java
        )
            .setParameter("restaurantId", restaurantId)
            .resultList
    }

    @Throws(PersistenceException::class)
    fun findOneWithRestaurant(customerId: Long): Waiting {
        return em.createQuery(
            "select m from Waiting m" +
                    " left join fetch m.restaurant where m.customer.id =:customerId", Waiting::class.java
        )
            .setParameter("customerId", customerId)
            .singleResult
    }

    fun findOneWithCustomerAndRestaurant(waitingId: Long): Waiting {
        return em.createQuery(
            "select m from Waiting m" +
                    " left join fetch m.customer" +
                    " left join fetch m.restaurant where m.id =:waitingId", Waiting::class.java
        )
            .setParameter("waitingId", waitingId)
            .singleResult
    }

    fun getCountInFrontOfMe(myWaitingNum: Int, restaurantId: Long): Long {
        val result = jdbcTemplate.queryForMap(
            "select count(*) as in_front_of_me from " +
                    "(select * from waiting where restaurant_id = ?) f where f.my_waiting_number < ?",
            restaurantId,
            myWaitingNum
        )
        return result["in_front_of_me"] as Long
    }
}
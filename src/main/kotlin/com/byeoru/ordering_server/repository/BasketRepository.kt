package com.byeoru.ordering_server.repository

import com.byeoru.ordering_server.domain.Basket
import com.byeoru.ordering_server.domain.dto.request.BasketPutDto
import javax.persistence.EntityManager
import org.springframework.jdbc.core.JdbcTemplate
import kotlin.Throws
import javax.persistence.PersistenceException
import java.lang.StringBuilder
import org.springframework.stereotype.Repository
import java.util.function.Consumer

@Repository
class BasketRepository(private val em: EntityManager,
                       private val jdbcTemplate: JdbcTemplate) {

    fun save(basket: Basket?) = em.persist(basket)

    @Throws(PersistenceException::class)
    fun findOneByCustomerIdAndFoodId(customerId: Long, foodId: Long): Basket {
        return em.createQuery(
            "select m from Basket m" +
                    " where m.customer.id =:customerId and m.food.id =:foodId", Basket::class.java
        )
            .setParameter("customerId", customerId)
            .setParameter("foodId", foodId)
            .singleResult
    }

    fun putCount(customerId: Long, basketPutDtos: List<BasketPutDto>) {

        // Query builder
        val caseQueryBuilder = StringBuilder(30 * basketPutDtos.size)

        // list 요소 수만큼 쿼리를 추가
        basketPutDtos.forEach(Consumer { dto: BasketPutDto ->
            caseQueryBuilder.append(
                String.format(
                    " when %d then %d",
                    dto.basketId,
                    dto.count
                )
            )
        })
        caseQueryBuilder.append(" else m.count end")

        // 한 번의 Query로 여러 음식의 count를 UPDATE
        em.createQuery(
            "update Basket m set m.count = case m.id"
                    + caseQueryBuilder
                    + " where m.customer.id =:customerId"
        )
            .setParameter("customerId", customerId)
            .executeUpdate()
    }

    fun remove(basketId: Long) {
        val basket = em.find(Basket::class.java, basketId)
        em.remove(basket)
    }

    fun removeAll(customerId: Long) {
        em.createQuery("delete from Basket m where m.customer.id =:id")
            .setParameter("id", customerId)
            .executeUpdate()
    }

    fun getBasketCount(customerId: Long): Int {
        val result = jdbcTemplate.queryForMap(
            "select ifnull(sum(count), 0) as basket_count from basket where customer_id=?;",
            customerId
        )
        return result["basket_count"].toString().toInt()
    }
}
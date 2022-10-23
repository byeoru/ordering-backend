package com.byeoru.ordering_server.repository

import com.byeoru.ordering_server.domain.Food
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceException

@Repository
class FoodRepository(private val em: EntityManager,
                     private val jdbcTemplate: JdbcTemplate) {

    fun save(food: Food) {
        em.persist(food)
    }

    fun findOne(id: Long): Food {
        return em.find(Food::class.java, id)
    }

    fun findAll(restaurantId: Long): List<Food> {
        return em.createQuery("select distinct m from Food m where m.restaurant.id =:id", Food::class.java)
            .setParameter("id", restaurantId)
            .resultList
    }

    @Throws(PersistenceException::class)
    fun remove(food: Food) {
        em.remove(food)
    }

    fun remove(foodId: Long) {
        jdbcTemplate.update("delete from food where food_id=?", foodId)
    }
}
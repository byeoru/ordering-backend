package com.byeoru.ordering_server.repository

import com.byeoru.ordering_server.domain.RepresentativeMenu
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceException

@Repository
class RepresentativeMenuRepository(private val em: EntityManager) {

    fun save(representativeMenu: RepresentativeMenu) = em.persist(representativeMenu)

    fun remove(id: Long) {
        val representativeMenu = em.find(RepresentativeMenu::class.java, id)
        em.remove(representativeMenu)
    }

    @Throws(PersistenceException::class)
    fun findOneByFoodId(foodId: Long) {
        em.createQuery("select m from RepresentativeMenu m where m.food.id =:foodId", RepresentativeMenu::class.java)
            .setParameter("foodId", foodId)
            .singleResult
    }

    fun findAllByRestaurantId(restaurantId: Long): List<RepresentativeMenu> {
        return em.createQuery(
            "select distinct m from RepresentativeMenu m" +
                    " where m.restaurant.id =:restaurantId", RepresentativeMenu::class.java
        )
            .setParameter("restaurantId", restaurantId)
            .resultList
    }
}
package com.byeoru.ordering_server.repository

import com.byeoru.ordering_server.domain.member.Owner
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceException

@Repository
class OwnerRepository(private val em: EntityManager) : MemberRepository<Owner> {

    @Throws(PersistenceException::class)
    override fun save(owner: Owner) {
        em.persist(owner)
    }

    override fun findOne(id: Long): Owner {
        return em.find(Owner::class.java, id)
    }

    fun findOneWithRestaurant(ownerId: Long): Owner {
        return em.createQuery(
            "select m from Owner m left join fetch m.restaurant where m.id=:ownerId",
            Owner::class.java
        )
            .setParameter("ownerId", ownerId)
            .singleResult
    }

    @Throws(PersistenceException::class)
    override fun findById(signInId: String): Owner {
        return em.createQuery("select m from Owner m where m.signInId=:signInId", Owner::class.java)
            .setParameter("signInId", signInId)
            .singleResult
    }

    override fun remove(id: Long) {
        val owner = em.find(Owner::class.java, id)
        em.remove(owner)
    }

    @Throws(PersistenceException::class)
    fun findOneWithRestaurantById(signInId: String): Owner {
        return em.createQuery(
            "select m from Owner m left join fetch m.restaurant " +
                    "where m.signInId =:signInId", Owner::class.java
        )
            .setParameter("signInId", signInId)
            .singleResult
    }

    @Throws(PersistenceException::class)
    fun findOneWithPhoneNumber(ownerId: Long): Owner {
        return em.createQuery(
            "select m from Owner m" +
                    " left join fetch m.phoneNumber where m.id =:id", Owner::class.java
        )
            .setParameter("id", ownerId)
            .singleResult
    }
}
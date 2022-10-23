package com.byeoru.ordering_server.repository

import com.byeoru.ordering_server.domain.member.Customer
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceException

@Repository
class CustomerRepository(private val em: EntityManager) : MemberRepository<Customer> {

    @Throws(PersistenceException::class)
    override fun save(member: Customer) = em.persist(member)

    override fun findOne(id: Long) = em.find(Customer::class.java, id)

    fun findOneWithBasket(id: Long): Customer {
        return em.createQuery(
            "select distinct m from Customer m" +
                    " left join fetch m.baskets where m.id =:id", Customer::class.java
        )
            .setParameter("id", id)
            .singleResult
    }

    fun findOneWithBasketWithFood(id: Long): Customer {
        return em.createQuery(
            "select distinct m from Customer m" +
                    " left join fetch m.baskets b" +
                    " left join fetch b.food where m.id =:id", Customer::class.java
        )
            .setParameter("id", id)
            .singleResult
    }

    @Throws(PersistenceException::class)
    override fun findById(signInId: String): Customer {
        return em.createQuery("select m from Customer m where m.signInId =:signInId", Customer::class.java)
            .setParameter("signInId", signInId)
            .singleResult
    }

    @Throws(PersistenceException::class)
    fun findOneWithPhoneNumber(customerId: Long?): Customer {
        return em.createQuery(
            "select m from Customer m" +
                    " left join fetch m.phoneNumber where m.id =:id", Customer::class.java
        )
            .setParameter("id", customerId)
            .singleResult
    }

    @Throws(PersistenceException::class)
    fun findOneWithPhoneNumberWithWaiting(customerId: Long?): Customer {
        return em.createQuery(
            "select m from Customer m" +
                    " left join fetch m.waiting" +
                    " left join fetch m.phoneNumber where m.id =:id", Customer::class.java
        )
            .setParameter("id", customerId)
            .singleResult
    }

    @Throws(PersistenceException::class)
    override fun remove(id: Long) {
        val customer = em.find(Customer::class.java, id)
        em.remove(customer)
    }
}
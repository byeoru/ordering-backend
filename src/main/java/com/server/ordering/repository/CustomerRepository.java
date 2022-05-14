package com.server.ordering.repository;

import com.server.ordering.domain.member.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomerRepository implements MemberRepository<Customer> {

    private final EntityManager em;

    @Override
    public void save(Customer customer) throws PersistenceException {
        em.persist(customer);
    }

    @Override
    public Customer findOne(Long id) {
        return em.find(Customer.class, id);
    }

    public Customer findOneWithBasket(Long id) {
        return em.createQuery("select distinct m from Customer m left join fetch m.baskets where m.id = :id", Customer.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public Customer findById(String signInId) throws PersistenceException {
        return em.createQuery("select m from Customer m where m.signInId = :signInId", Customer.class)
                .setParameter("signInId", signInId)
                .getSingleResult();
    }

    @Override
    public Customer findByIdAndPassword(String signInId, String password) throws PersistenceException {
        return em.createQuery("select c from Customer c where c.signInId = :signInId and c.password = :password", Customer.class)
                .setParameter("signInId", signInId)
                .setParameter("password", password)
                .getSingleResult();
    }

    public Customer findOneWithPhoneNumber(Long customerId) throws PersistenceException {
        return em.createQuery("select m from Customer m left join fetch m.phoneNumber where m.id = :id", Customer.class)
                .setParameter("id", customerId)
                .getSingleResult();
    }

    public Customer findOneWithWaiting(Long customerId) throws PersistenceException {
        return em.createQuery("select m from Customer m left join fetch m.waiting where m.id = :id", Customer.class)
                .setParameter("id", customerId)
                .getSingleResult();
    }

    @Override
    public void remove(Long id) throws PersistenceException {
        Customer customer = em.find(Customer.class, id);
        em.remove(customer);
    }

    // Test
    @Transactional
    public List<Customer> findAll() {
        return em.createQuery("select m from Customer m", Customer.class)
                .getResultList();
    }
}

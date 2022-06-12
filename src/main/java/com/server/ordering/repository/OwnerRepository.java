package com.server.ordering.repository;

import com.server.ordering.domain.member.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OwnerRepository implements MemberRepository<Owner> {

    private final EntityManager em;

    @Override
    public void save(Owner owner) throws PersistenceException {
        em.persist(owner);
    }

    @Override
    public Owner findOne(Long ownerId) {
        return em.find(Owner.class, ownerId);
    }

    public Owner findOneWithRestaurant(Long ownerId) {
        return em.createQuery("select m from Owner m left join fetch m.restaurant where m.id=:ownerId", Owner.class)
                .setParameter("ownerId", ownerId)
                .getSingleResult();
    }

    @Override
    public Owner findById(String signInId) throws PersistenceException {
        return em.createQuery("select m from Owner m where m.signInId=:signInId", Owner.class)
                .setParameter("signInId", signInId)
                .getSingleResult();
    }

    @Override
    public void remove(Long id) {
        Owner owner = em.find(Owner.class, id);
        em.remove(owner);
    }

    public Owner findOneWithRestaurantById(String signInId) throws PersistenceException {
        return em.createQuery("select m from Owner m left join fetch m.restaurant " +
                "where m.signInId =:signInId", Owner.class)
                .setParameter("signInId", signInId)
                .getSingleResult();
    }

    public Owner findOneWithPhoneNumber(Long ownerId) throws PersistenceException {
        return em.createQuery("select m from Owner m" +
                        " left join fetch m.phoneNumber where m.id =:id", Owner.class)
                .setParameter("id", ownerId)
                .getSingleResult();
    }
}

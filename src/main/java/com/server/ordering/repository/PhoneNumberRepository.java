package com.server.ordering.repository;

import com.server.ordering.domain.MemberType;
import com.server.ordering.domain.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Repository
@RequiredArgsConstructor
public class PhoneNumberRepository {

    private final EntityManager em;

    public void save(PhoneNumber phoneNumber) {
        em.persist(phoneNumber);
    }

    public PhoneNumber findPhoneNumber(MemberType memberType, String phoneNumber) throws NoResultException {
        return em.createQuery("select m from PhoneNumber m " +
                "where m.phoneNumber = :phoneNumber and m.memberType = :memberType", PhoneNumber.class)
                .setParameter("phoneNumber", phoneNumber)
                .setParameter("memberType", memberType)
                .getSingleResult();
    }
}

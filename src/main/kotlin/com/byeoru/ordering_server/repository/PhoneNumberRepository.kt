package com.byeoru.ordering_server.repository

import com.byeoru.ordering_server.domain.MemberType
import com.byeoru.ordering_server.domain.PhoneNumber
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.NoResultException

@Repository
class PhoneNumberRepository(private val em: EntityManager) {

    fun save(phoneNumber: PhoneNumber) {
        em.persist(phoneNumber)
    }

    @Throws(NoResultException::class)
    fun findPhoneNumber(memberType: MemberType, phoneNumber: String) {
        em.createQuery(
            "select m from PhoneNumber m " +
                    "where m.phoneNumber = :phoneNumber and m.memberType = :memberType", PhoneNumber::class.java
        )
            .setParameter("phoneNumber", phoneNumber)
            .setParameter("memberType", memberType)
            .singleResult
    }
}
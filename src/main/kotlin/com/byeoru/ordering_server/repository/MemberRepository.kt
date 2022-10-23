package com.byeoru.ordering_server.repository

import com.byeoru.ordering_server.domain.member.MemberBase

interface MemberRepository<T : MemberBase> {
    fun save(member: T)
    fun findOne(id: Long): T
    fun findById(signInId: String): T
    fun remove(id: Long)
}
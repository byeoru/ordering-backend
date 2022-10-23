package com.byeoru.ordering_server.service

import com.byeoru.ordering_server.domain.dto.request.SignInDto
import com.byeoru.ordering_server.domain.member.MemberBase

interface MemberService<T : MemberBase> {
    fun signUp(signUpDto: Any): Long?
    fun signIn(signInDto: SignInDto): T?
    fun signOut(id: Long)
    fun isIdDuplicated(signInId: String): Boolean
    fun deleteAccount(id: Long)
}
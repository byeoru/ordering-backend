package com.byeoru.ordering_server.service

import com.byeoru.ordering_server.domain.MemberType
import com.byeoru.ordering_server.domain.PhoneNumber
import com.byeoru.ordering_server.domain.dto.request.OwnerSignUpDto
import com.byeoru.ordering_server.domain.dto.request.PasswordChangeDto
import com.byeoru.ordering_server.domain.dto.request.SignInDto
import com.byeoru.ordering_server.domain.member.Owner
import com.byeoru.ordering_server.repository.OwnerRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.NoResultException
import javax.persistence.PersistenceException

@Service
@Transactional(readOnly = true)
class OwnerService(private val ownerRepository: OwnerRepository,
                   private val passwordEncoder: PasswordEncoder) : MemberService<Owner> {

    /**
     * 점주 회원가입
     */
    @Transactional
    @Throws(PersistenceException::class)
    override fun signUp(signUpDto: Any): Long? {
        val ownerSignUpDto = signUpDto as OwnerSignUpDto
        val phoneNumber = PhoneNumber(ownerSignUpDto.phoneNumber, MemberType.OWNER)
        val encodedPassword = passwordEncoder.encode(ownerSignUpDto.password)
        val owner = Owner(ownerSignUpDto.signInId, encodedPassword, phoneNumber)
        ownerRepository.save(owner)
        return owner.id
    }

    /**
     * 점주 로그인
     */
    @Transactional
    override fun signIn(signInDto: SignInDto): Owner? {
        return try {
            val owner = ownerRepository.findOneWithRestaurantById(signInDto.signInId)
            val bMatch = passwordEncoder.matches(signInDto.password, owner.password)
            if (!bMatch) {
                return null
            }
            owner.putFirebaseToken(signInDto.firebaseToken)
            owner
        } catch (e: NoResultException) {
            null
        }
    }

    @Transactional
    override fun signOut(id: Long) {
        val owner = ownerRepository.findOne(id)
        owner.clearFirebaseToken()
    }

    /**
     * 점주 회원가입할 때 email 중복 검증
     */
    @Throws(PersistenceException::class)
    override fun isIdDuplicated(signInId: String): Boolean {
        return try {
            ownerRepository.findById(signInId)
            true
        } catch (e: NoResultException) {
            false
        }
    }

    /**
     * 점주 휴대폰번호 변경
     */
    @Transactional
    fun putPhoneNumber(ownerId: Long, phoneNumber: String) {
        val owner = ownerRepository.findOneWithPhoneNumber(ownerId)
        owner.phoneNumber.putPhoneNumber(phoneNumber)
    }

    /**
     * 점주 비밀번호 변경
     */
    @Transactional
    fun putPassword(ownerId: Long, dto: PasswordChangeDto): Boolean {
        val owner = ownerRepository.findOne(ownerId)
        if (passwordEncoder.matches(dto.currentPassword, owner.password)) {
            val encodedNewPassword = passwordEncoder.encode(dto.newPassword)
            owner.putPassword(encodedNewPassword)
            return true
        }
        return false
    }

    /**
     * 점주 회원탈퇴
     */
    @Transactional
    override fun deleteAccount(id: Long) = ownerRepository.remove(id)

    companion object {
        private val log = LoggerFactory.getLogger(OwnerService::class.java)
    }
}
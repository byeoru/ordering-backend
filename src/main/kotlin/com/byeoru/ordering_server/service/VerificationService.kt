package com.byeoru.ordering_server.service

import com.byeoru.ordering_server.Properties
import com.byeoru.ordering_server.domain.MemberType
import com.byeoru.ordering_server.repository.PhoneNumberRepository
import com.twilio.Twilio
import com.twilio.rest.verify.v2.service.Verification
import com.twilio.rest.verify.v2.service.VerificationCheck
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.NoResultException
import javax.persistence.PersistenceException

@Service
@Transactional(readOnly = true)
class VerificationService(private val phoneNumberRepository: PhoneNumberRepository,
                          private val pt: Properties) {

    @Throws(PersistenceException::class)
    fun isPhoneNumberDuplicated(memberType: MemberType, phoneNumber: String): Boolean {
        return try {
            phoneNumberRepository.findPhoneNumber(memberType, phoneNumber)
            true
        } catch (e: NoResultException) {
            false
        }
    }

    fun sendCode(phoneNum: String) {
        Twilio.init(pt.account_sid, pt.auth_token)
        Verification.creator(pt.path_service_sid, "+82$phoneNum", "sms")
            .setLocale("ko")
            .create()
    }

    fun checkCode(totalNum: String, verificationCode: String): Boolean {
        Twilio.init(pt.account_sid, pt.auth_token)
        val verificationCheck = VerificationCheck.creator(pt.path_service_sid, verificationCode)
            .setTo(totalNum).create()
        return verificationCheck.valid
    }
}
package com.byeoru.ordering_server.domain.member

import com.byeoru.ordering_server.domain.PhoneNumber
import javax.persistence.*

@MappedSuperclass
abstract class MemberBase(signInId: String,
                          password: String,
                          phoneNumber: PhoneNumber) {

    var signInId: String = signInId
        protected set
    var password: String = password
        protected set

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "phone_number")
    var phoneNumber: PhoneNumber = phoneNumber
        protected set

    var firebaseToken: String? = null
        protected set

    fun putPassword(password: String) {
        this.password = password
    }

    fun putFirebaseToken(firebaseToken: String?) {
        this.firebaseToken = firebaseToken
    }

    fun clearFirebaseToken() {
        firebaseToken = null
    }
}
package com.byeoru.ordering_server.domain

import javax.persistence.*

@Entity
class PhoneNumber(
    phoneNumber: String,
    memberType: MemberType) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_number_id")
     var id: Long? = null
         protected set

     var phoneNumber: String = phoneNumber
         protected set

    @Enumerated(value = EnumType.STRING)
     var memberType: MemberType = memberType
         protected set

    fun putPhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }
}
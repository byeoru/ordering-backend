package com.byeoru.ordering_server.domain

import com.byeoru.ordering_server.domain.member.Customer
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
class Waiting(restaurant: Restaurant,
              customer: Customer,
              myWaitingNumber: Int,
              numOfTeamMembers: Byte,
              phoneNumber: PhoneNumber) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waiting_id")
     var id: Long? = null
         protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
     var restaurant: Restaurant = restaurant
         protected set

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
     var customer: Customer = customer
         protected set

     var myWaitingNumber: Int = myWaitingNumber
         protected set

     var numOfTeamMembers: Byte = numOfTeamMembers
         protected set

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phone_number")
     var phoneNumber: PhoneNumber = phoneNumber
         protected set

     var waitingRegisterTime: ZonedDateTime? = null
         protected set

    fun registerWaitingTime() {
        waitingRegisterTime = ZonedDateTime.now()
    }
}
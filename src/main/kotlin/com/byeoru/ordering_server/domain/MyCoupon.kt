package com.byeoru.ordering_server.domain

import com.byeoru.ordering_server.domain.member.Customer
import javax.persistence.*

@Entity
class MyCoupon(serialNumber: String,
               value: Int,
               customer: Customer) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_coupon_id")
     var id: Long? = null
         protected set

     var serialNumber: String = serialNumber
         protected set

     var value = value
         protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
     var customer: Customer = customer
         protected set
}
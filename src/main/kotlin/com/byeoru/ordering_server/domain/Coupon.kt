package com.byeoru.ordering_server.domain

import javax.persistence.*

@Entity
class Coupon(serialNumber: String,
             value: Int) {

    @Id
     var serialNumber: String = serialNumber
         protected set

     var value = value
         protected set
}
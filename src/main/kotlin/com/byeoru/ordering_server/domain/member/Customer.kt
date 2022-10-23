package com.byeoru.ordering_server.domain.member

import com.byeoru.ordering_server.domain.*
import java.util.ArrayList
import javax.persistence.*

@Entity
class Customer(nickname: String,
               signInId: String,
               password: String,
               phoneNumber: PhoneNumber) : MemberBase(signInId, password, phoneNumber) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    var id: Long? = null
         protected set

     var nickname: String = nickname
         protected set

     var basketKey: Long? = null
         protected set

    @OneToMany(mappedBy = "customer", cascade = [CascadeType.REMOVE])
     var bookmarks: MutableList<Bookmark> = ArrayList<Bookmark>()
         protected set

    @OneToMany(mappedBy = "customer", cascade = [CascadeType.REMOVE], orphanRemoval = true)
     var baskets: MutableList<Basket> = ArrayList<Basket>()
         protected set

    @OneToMany(mappedBy = "customer")
     var orders: MutableList<Order> = ArrayList<Order>()
         protected set

    @OneToMany(mappedBy = "customer", cascade = [CascadeType.REMOVE], orphanRemoval = true)
     var coupons: MutableList<MyCoupon> = ArrayList<MyCoupon>()
         protected set

    @OneToOne(mappedBy = "customer", cascade = [CascadeType.REMOVE], orphanRemoval = true)
     var waiting: Waiting? = null
         protected set

    fun changeBasketKey(restaurantId: Long?) {
        basketKey = restaurantId
    }

    fun isAbleToWaiting() = waiting == null
}
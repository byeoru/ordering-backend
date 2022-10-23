package com.byeoru.ordering_server.domain

import com.byeoru.ordering_server.domain.member.Customer
import javax.persistence.*

@Entity
class Bookmark(restaurant: Restaurant,
               customer: Customer) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
     var id: Long? = null
         protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
     var restaurant: Restaurant = restaurant
         protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
     var customer: Customer = customer
         protected set
}
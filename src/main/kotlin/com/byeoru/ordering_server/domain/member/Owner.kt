package com.byeoru.ordering_server.domain.member

import com.byeoru.ordering_server.domain.PhoneNumber
import com.byeoru.ordering_server.domain.Restaurant
import javax.persistence.*

@Entity
class Owner(signInId: String,
            password: String,
            phoneNumber: PhoneNumber) : MemberBase(signInId, password, phoneNumber) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id")
     var id: Long? = null
         protected set

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE], orphanRemoval = true)
    @JoinColumn(name = "restaurant_id")
     var restaurant: Restaurant? = null
         protected set

    fun registerRestaurant(restaurant: Restaurant?) {
        this.restaurant = restaurant
    }
}
package com.byeoru.ordering_server.domain

import javax.persistence.*

@Entity
class RepresentativeMenu(restaurant: Restaurant,
                         food: Food,
                         foodName: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "representative_menu_id")
     var id: Long? = null
         protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
     var restaurant: Restaurant = restaurant
         protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
     var food: Food = food
         protected set

     var foodName: String = foodName
         protected set

    fun registerRestaurant(restaurant: Restaurant) {
        this.restaurant = restaurant
    }
}
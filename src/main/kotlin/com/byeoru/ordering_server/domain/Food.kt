package com.byeoru.ordering_server.domain

import javax.persistence.*

@Entity
class Food(foodName: String,
                 price: Int,
                 soldOut: Boolean,
                 imageUrl: String?,
                 menuIntro: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    var id: Long? = null
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    var restaurant: Restaurant? = null
        protected set

    var foodName: String = foodName
        protected set

    var price = price
        protected set

    var isSoldOut = soldOut
        protected set

    var imageUrl: String? = imageUrl
        protected set

    var menuIntro: String = menuIntro
        protected set

    fun changeSoldOutStatus(soldOut: Boolean) {
        isSoldOut = soldOut
    }

    fun registerRestaurant(restaurant: Restaurant?) {
        this.restaurant = restaurant
    }

    fun putFood(foodName: String, price: Int, menuIntro: String) {
        this.foodName = foodName
        this.price = price
        this.menuIntro = menuIntro
    }

    fun putFood(foodName: String, price: Int, menuIntro: String, imageUrl: String?) {
        this.foodName = foodName
        this.price = price
        this.menuIntro = menuIntro
        this.imageUrl = imageUrl
    }
}
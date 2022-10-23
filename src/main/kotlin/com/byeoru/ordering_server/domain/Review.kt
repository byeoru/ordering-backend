package com.byeoru.ordering_server.domain

import javax.persistence.*

@Entity
class Review(review: String,
             rating: Float) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
     var id: Long? = null
         protected set

     var review: String = review
         protected set

    @Column(nullable = false)
     var rating: Float = rating
         protected set

     var imageUrl: String? = null
         protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private var restaurant: Restaurant? = null

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
     lateinit var order: Order
         protected set

    fun registerOrderAndRestaurant(order: Order, restaurant: Restaurant) {
        this.order = order
        this.restaurant = restaurant
        order.registerReview(this)
        restaurant.registerReview(this)
    }

    fun putReview(text: String, rating: Float) {
        review = text
        this.rating = rating
    }

    fun registerImageUrl(imageUrl: String?) {
        this.imageUrl = imageUrl
    }
}
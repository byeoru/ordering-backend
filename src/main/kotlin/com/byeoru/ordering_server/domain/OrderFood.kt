package com.byeoru.ordering_server.domain

import javax.persistence.*

@Entity
class OrderFood(food: Food,
                price: Int,
                count: Int) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_food_id")
     var id: Long? = null
         protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
     var food: Food = food
         protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
     var order: Order? = null
         protected set

     var price = price
         protected set

     var count = count
         protected set

    fun registerOrder(order: Order) {
        this.order = order
    }

    companion object {
        @JvmStatic
        fun createOrderFood(food: Food, price: Int, count: Int): OrderFood {
            return OrderFood(food, price, count)
        }
    }
}
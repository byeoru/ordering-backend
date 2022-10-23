package com.byeoru.ordering_server.domain

import com.byeoru.ordering_server.domain.member.Customer
import javax.persistence.*

@Entity
class Basket(customer: Customer,
             food: Food,
             count: Int) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_id")
     var id: Long? = null
         protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
     var customer: Customer = customer
         protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
     var food: Food = food
         protected set

     var count = count
         protected set

    fun updateCount(count: Int) {
        this.count = count
    }

    companion object {
        @JvmStatic
        fun CreateBasket(customer: Customer, food: Food, count: Int): Basket {
            return Basket(customer, food, count)
        }
    }
}
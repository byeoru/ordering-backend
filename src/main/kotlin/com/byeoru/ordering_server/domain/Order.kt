package com.byeoru.ordering_server.domain

import com.byeoru.ordering_server.domain.member.Customer
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "orders")
class Order(customer: Customer,
            restaurant: Restaurant,
            status: OrderStatus,
            orderType: OrderType,
            tableNumber: Int,
            totalPrice: Int,
            orderSummary: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
     var id: Long? = null
         protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
     var customer: Customer = customer
         protected set

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
     var orderFoods: MutableSet<OrderFood> = HashSet<OrderFood>()
         protected set

     var receivedTime: ZonedDateTime? = null
         protected set

     var checkedTime: ZonedDateTime? = null
         protected set

     var canceledOrCompletedTime: ZonedDateTime? = null
         protected set

    var myOrderNumber: Int? = null

    @OneToOne(mappedBy = "order")
     var review: Review? = null
         protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
     var restaurant: Restaurant = restaurant
         protected set

    @Enumerated(value = EnumType.STRING)
     var status: OrderStatus = status
         protected set

    @Enumerated(value = EnumType.STRING)
     var orderType: OrderType = orderType
         protected set

     var tableNumber: Int = tableNumber
         protected set

    @Column(nullable = false)
     var totalPrice: Int = totalPrice
         protected set

     var orderSummary: String = orderSummary
         protected set

    fun addOrderFood(orderFood: OrderFood) {
        orderFoods.add(orderFood)
        orderFood.registerOrder(this)
    }

    val isAbleToCancel = status == OrderStatus.ORDERED
    val isAbleToOwnerCancel = status == OrderStatus.ORDERED || status == OrderStatus.CHECKED
    val isAbleToCheck = status == OrderStatus.ORDERED
    val isAbleToComplete = status == OrderStatus.CHECKED

    fun registerReview(review: Review?) {
        this.review = review
    }

    val isAbleRegisterReview: Boolean = Objects.isNull(review)

    fun registerReceivedTime() {
        receivedTime = ZonedDateTime.now()
    }

    fun registerCheckedTime() {
        checkedTime = ZonedDateTime.now()
    }

    fun registerCanceledTime() {
        canceledOrCompletedTime = ZonedDateTime.now()
    }

    fun registerCompletedTime() {
        canceledOrCompletedTime = ZonedDateTime.now()
    }

    fun cancel() {
        status = OrderStatus.CANCELED
    }

    fun check() {
        status = OrderStatus.CHECKED
    }

    fun complete() {
        status = OrderStatus.COMPLETED
    }

    fun removeAllOrderFood() {
        orderFoods.clear()
    }

    fun registerMyOrderNumber(orderNumber: Int) {
        myOrderNumber = orderNumber
    }

    companion object {
        @JvmStatic
        fun createOrder(
            customer: Customer, restaurant: Restaurant, orderFoods: List<OrderFood>,
            orderType: OrderType, tableNumber: Int, totalPrice: Int, orderSummary: String
        ): Order {
            val order =
                Order(customer, restaurant, OrderStatus.ORDERED, orderType, tableNumber, totalPrice, orderSummary)
            orderFoods.forEach(order::addOrderFood)
            return order
        }
    }
}
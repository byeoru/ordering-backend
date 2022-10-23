package com.byeoru.ordering_server.domain.dto.response

import com.byeoru.ordering_server.domain.Order
import com.byeoru.ordering_server.domain.OrderStatus
import com.byeoru.ordering_server.domain.OrderType
import java.time.format.DateTimeFormatter

open class OrderPreviewDto(order: Order) {

    val orderId: Long?
    val reviewId: Long?
    val myOrderNumber: Int?
    val orderSummary: String?
    val receivedTime: String?
    val checkTime: String?
    val cancelOrCompletedTime: String?
    val totalPrice: Int
    val orderType: OrderType
    val tableNumber: Int?
    val orderStatus: OrderStatus?

    init {
        this.orderId = order.id
        this.reviewId = order.review?.id
        this.myOrderNumber = order.myOrderNumber
        this.orderSummary = order.orderSummary
        this.receivedTime = order.receivedTime?.format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss"))
        this.checkTime = order.checkedTime?.format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss"))
        this.cancelOrCompletedTime = order.canceledOrCompletedTime?.format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss"))
        this.totalPrice = order.totalPrice
        this.orderType = order.orderType
        this.tableNumber = order.tableNumber
        this.orderStatus = order.status
    }
}
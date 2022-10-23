package com.byeoru.ordering_server.domain.dto.response

import com.byeoru.ordering_server.domain.Order

data class OrderDetailDto(val order: Order) {

    val orderFoods: List<OrderFoodDto>

    init {
        this.orderFoods = order.orderFoods.map { OrderFoodDto(it) }
    }
}
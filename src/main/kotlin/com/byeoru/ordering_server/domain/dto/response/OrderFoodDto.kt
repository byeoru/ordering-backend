package com.byeoru.ordering_server.domain.dto.response

import com.byeoru.ordering_server.domain.OrderFood

class OrderFoodDto(orderFood: OrderFood) {

    val foodName: String
    val price: Int
    val count: Int

    init {
        foodName = orderFood.food.foodName
        price = orderFood.price
        count = orderFood.count
    }
}
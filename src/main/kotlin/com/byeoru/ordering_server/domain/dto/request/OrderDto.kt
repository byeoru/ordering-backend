package com.byeoru.ordering_server.domain.dto.request

import com.byeoru.ordering_server.domain.OrderType

data class OrderDto(val tableNumber: Int,
                    val orderType: OrderType)
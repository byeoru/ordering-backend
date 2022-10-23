package com.byeoru.ordering_server.domain.dto.request

data class FoodWithRestaurantIdDto(val restaurantId: Long,
                                   val foodName: String,
                                   val price: Int,
                                   val soldOut: Boolean)
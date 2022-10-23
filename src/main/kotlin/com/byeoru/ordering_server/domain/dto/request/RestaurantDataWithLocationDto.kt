package com.byeoru.ordering_server.domain.dto.request

import com.byeoru.ordering_server.domain.FoodCategory
import com.byeoru.ordering_server.domain.RestaurantType

data class RestaurantDataWithLocationDto(val latitude: Double,
                                         val longitude: Double,
                                         val restaurantName: String,
                                         val ownerName: String,
                                         val address: String,
                                         val tableCount: Int,
                                         val foodCategory: FoodCategory,
                                         val restaurantType: RestaurantType,
                                         val admissionWaitingTime: Int,
                                         val orderingWaitingTime: Int)
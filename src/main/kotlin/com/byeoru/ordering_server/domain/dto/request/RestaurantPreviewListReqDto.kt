package com.byeoru.ordering_server.domain.dto.request

import com.byeoru.ordering_server.domain.FoodCategory

data class RestaurantPreviewListReqDto(val latitude: Double,
                                       val longitude: Double,
                                       val foodCategory: FoodCategory)
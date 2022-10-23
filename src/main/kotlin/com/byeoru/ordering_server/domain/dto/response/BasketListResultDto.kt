package com.byeoru.ordering_server.domain.dto.response

data class BasketListResultDto(val restaurantName: String?,
                               val basketFoods: List<BasketFood>)
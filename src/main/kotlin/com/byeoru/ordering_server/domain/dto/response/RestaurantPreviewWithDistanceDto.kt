package com.byeoru.ordering_server.domain.dto.response

data class RestaurantPreviewWithDistanceDto(val distanceMeter: Int,
                                            val restaurantId: Long,
                                            val restaurantName: String,
                                            val profileImageUrl: String,
                                            val backgroundImageUrl: String,
                                            val representativeMenus: MutableList<String> = ArrayList()) {

    fun addRepresentativeFoodName(foodName: String) = representativeMenus.add(foodName)
}

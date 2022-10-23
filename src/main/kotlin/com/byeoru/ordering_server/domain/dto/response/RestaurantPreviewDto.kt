package com.byeoru.ordering_server.domain.dto.response

import com.byeoru.ordering_server.domain.Restaurant

class RestaurantPreviewDto {

    val restaurantId: Long?
    val restaurantName: String
    val profileImageUrl: String?
    val backgroundImageUrl: String?
    val representativeMenus: MutableList<String> = ArrayList()

    fun addRepresentativeFoodName(foodName: String) {
        representativeMenus.add(foodName)
    }

    constructor(restaurant: Restaurant) {
        this.restaurantId = restaurant.id
        this.restaurantName = restaurant.restaurantName
        this.profileImageUrl = restaurant.profileImageUrl
        this.backgroundImageUrl = restaurant.backgroundImageUrl
        this.representativeMenus.addAll(restaurant.representativeMenus.map { it.foodName })
    }

    constructor(restaurantId: Long, restaurantName: String, profileImageUrl: String, backgroundImageUrl: String) {
        this.restaurantId = restaurantId
        this.restaurantName = restaurantName
        this.profileImageUrl = profileImageUrl
        this.backgroundImageUrl = backgroundImageUrl
    }
}
package com.byeoru.ordering_server.domain.dto.response

import com.byeoru.ordering_server.domain.FoodCategory
import com.byeoru.ordering_server.domain.Restaurant
import com.byeoru.ordering_server.domain.RestaurantType

class RestaurantDataWithImgUrlDto(restaurant: Restaurant) {

    val profileImageUrl: String?
    val backgroundImageUrl: String?

    val restaurantName: String
    val ownerName: String
    val address: String
    val tableCount: Int?
    val foodCategory: FoodCategory
    val restaurantType: RestaurantType
    val admissionWaitingTime: Int
    val orderingWaitingTime: Int

    init {
        this.profileImageUrl = restaurant.profileImageUrl
        this.backgroundImageUrl = restaurant.backgroundImageUrl
        this.restaurantName = restaurant.restaurantName
        this.ownerName = restaurant.ownerName
        this.address = restaurant.address
        this.tableCount = restaurant.tableCount
        this.foodCategory = restaurant.foodCategory
        this.restaurantType = restaurant.restaurantType
        this.admissionWaitingTime = restaurant.admissionWaitingTime
        this.orderingWaitingTime = restaurant.orderingWaitingTime
    }
}
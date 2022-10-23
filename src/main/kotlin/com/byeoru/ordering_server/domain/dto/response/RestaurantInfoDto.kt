package com.byeoru.ordering_server.domain.dto.response

import com.byeoru.ordering_server.domain.FoodCategory
import com.byeoru.ordering_server.domain.Restaurant
import com.byeoru.ordering_server.domain.RestaurantType

class RestaurantInfoDto(restaurant: Restaurant) {

    val ownerName: String
    val restaurantName: String
    val address: String
    val notice: String?
    val restaurantType: RestaurantType
    val foodCategory: FoodCategory
    val tableCount: Int?
    val orderingWaitingTime: Int
    val admissionWaitingTime: Int
    val latitude: Double
    val longitude: Double

    init {
        ownerName = restaurant.ownerName
        restaurantName = restaurant.restaurantName
        address = restaurant.address
        notice = restaurant.notice
        restaurantType = restaurant.restaurantType
        foodCategory = restaurant.foodCategory
        tableCount = restaurant.tableCount
        orderingWaitingTime = restaurant.orderingWaitingTime
        admissionWaitingTime = restaurant.admissionWaitingTime
        latitude = restaurant.location.y
        longitude = restaurant.location.x
    }
}
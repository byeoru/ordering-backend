package com.byeoru.ordering_server.domain.dto.response

import com.byeoru.ordering_server.domain.FoodCategory
import com.byeoru.ordering_server.domain.Restaurant
import com.byeoru.ordering_server.domain.RestaurantType

class OwnerSignInResultDto {

    var ownerId: Long?
    var restaurantId: Long? = null

    var profileImageUrl: String? = null
    var backgroundImageUrl: String? = null

    var restaurantName: String? = null
    var ownerName: String? = null
    var address: String? = null
    var tableCount: Int? = null
    var foodCategory: FoodCategory? = null
    var restaurantType: RestaurantType? = null
    var admissionWaitingTime: Int? = null
    var orderingWaitingTime: Int? = null

    constructor(ownerId: Long?) {
        this.ownerId = ownerId
    }

    constructor(ownerId: Long?, restaurant: Restaurant) : this(ownerId) {
        this.restaurantId = restaurant.id
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
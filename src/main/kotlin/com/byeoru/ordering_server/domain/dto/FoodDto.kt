package com.byeoru.ordering_server.domain.dto

import com.byeoru.ordering_server.domain.Food

class FoodDto {

    var foodName: String
    var price: Int
    var menuIntro: String

    // response 전용
    var foodId: Long?
    var imageUrl: String?
    var isSoldOut: Boolean

    constructor(foodName: String, price: Int, menuIntro: String, foodId: Long?, imageUrl: String?, soldOut: Boolean) {
        this.foodName = foodName
        this.price = price
        this.menuIntro = menuIntro
        this.foodId = foodId
        this.imageUrl = imageUrl
        this.isSoldOut = soldOut
    }

    constructor(food: Food) : this(
        food.foodName,
        food.price,
        food.menuIntro,
        food.id,
        food.imageUrl,
        food.isSoldOut
    )
}
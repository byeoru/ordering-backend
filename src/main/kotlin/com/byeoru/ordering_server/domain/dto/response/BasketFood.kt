package com.byeoru.ordering_server.domain.dto.response

import com.byeoru.ordering_server.domain.Basket

class BasketFood(basket: Basket) {

    val basketId: Long?
    val foodId: Long?
    val foodName: String
    val imageUrl: String?
    val soldOut: Boolean
    val price: Int
    val count: Int

    init {
        this.basketId = basket.id
        this.foodId = basket.food.id
        this.foodName = basket.food.foodName
        this.imageUrl = basket.food.imageUrl
        this.soldOut = basket.food.isSoldOut
        this.price = basket.food.price
        this.count = basket.count
    }
}
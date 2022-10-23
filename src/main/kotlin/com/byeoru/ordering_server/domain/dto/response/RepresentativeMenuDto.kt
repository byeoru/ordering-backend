package com.byeoru.ordering_server.domain.dto.response

import com.byeoru.ordering_server.domain.RepresentativeMenu

class RepresentativeMenuDto(representativeMenu: RepresentativeMenu) {

    val representativeMenuId: Long?

    val foodName: String
    val price: Int
    val menuIntro: String

    // response 전용
    val foodId: Long?
    val imageUrl: String?
    val isSoldOut: Boolean

    init {
        this.representativeMenuId = representativeMenu.id
        this.foodName = representativeMenu.food.foodName
        this.price = representativeMenu.food.price
        this.menuIntro = representativeMenu.food.menuIntro
        this.foodId = representativeMenu.food.id
        this.imageUrl = representativeMenu.food.imageUrl
        this.isSoldOut = representativeMenu.food.isSoldOut
    }
}
package com.byeoru.ordering_server.domain.dto.response

import com.byeoru.ordering_server.domain.Bookmark

data class BookmarkPreviewDto(val bookmark: Bookmark) {

    val bookmarkId: Long?
    val restaurantId: Long?
    val restaurantName: String
    val profileImageUrl: String?
    val backgroundImageUrl: String?
    val representativeMenus: MutableList<String> = ArrayList()

    init {
        this.bookmarkId = bookmark.id
        this.restaurantId = bookmark.restaurant.id
        this.restaurantName = bookmark.restaurant.restaurantName
        this.profileImageUrl = bookmark.restaurant.profileImageUrl
        this.backgroundImageUrl = bookmark.restaurant.backgroundImageUrl
        this.representativeMenus.addAll(bookmark.restaurant.representativeMenus.map { it.foodName })
    }
}
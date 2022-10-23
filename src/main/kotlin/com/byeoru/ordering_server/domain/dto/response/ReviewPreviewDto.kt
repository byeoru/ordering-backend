package com.byeoru.ordering_server.domain.dto.response

import com.byeoru.ordering_server.domain.Review

class ReviewPreviewDto(review: Review) {

    val reviewId: Long?
    val customerId: Long?
    val nickname: String
    val review: String
    val rating: Float
    val imageUrl: String?
    val orderSummary: String

    init {
        this.reviewId = review.id
        this.customerId = review.order.customer?.id
        this.nickname = review.order.customer?.nickname ?: "탈퇴한 회원"
        this.review = review.review
        this.rating = review.rating
        this.imageUrl = review.imageUrl
        this.orderSummary = review.order.orderSummary
    }
}
package com.byeoru.ordering_server.domain.dto.response

import com.byeoru.ordering_server.domain.Review
import com.byeoru.ordering_server.domain.member.Customer

class ReviewPreviewDto(review: Review) {

    val reviewId: Long?
    val customerId: Long?
    val nickname: String
    val review: String
    val rating: Float
    val imageUrl: String?
    val orderSummary: String

    init {
        val customer: Customer? = review.order?.customer
        this.reviewId = review.id
        this.customerId = if (customer == null) null else review.order!!.customer.id
        this.nickname = customer?.nickname ?: "탈퇴한 회원"
        this.review = review.review
        this.rating = review.rating
        this.imageUrl = review.imageUrl
        this.orderSummary = review.order!!.orderSummary
    }
}
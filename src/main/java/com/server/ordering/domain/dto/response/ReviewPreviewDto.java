package com.server.ordering.domain.dto.response;

import com.server.ordering.domain.Review;
import com.server.ordering.domain.member.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ReviewPreviewDto {

    private Long reviewId;
    private Long customerId;
    private String nickname;
    private String review;
    private float rating;
    private String imageUrl;
    private String orderSummary;

    public ReviewPreviewDto(Review review) {
        Customer customer = review.getOrder().getCustomer();
        this.reviewId = review.getId();
        this.customerId = customer == null ? null : review.getOrder().getCustomer().getId();
        this.nickname = customer == null ? "탈퇴한 회원" : customer.getNickname();
        this.review = review.getReview();
        this.rating = review.getRating();
        this.imageUrl = review.getImageUrl();
        this.orderSummary = review.getOrder().getOrderSummary();
    }
}

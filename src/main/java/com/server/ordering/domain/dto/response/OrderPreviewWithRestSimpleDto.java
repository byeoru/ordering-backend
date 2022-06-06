package com.server.ordering.domain.dto.response;

import com.server.ordering.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class OrderPreviewWithRestSimpleDto extends OrderPreviewDto {

    private Long restaurantId;
    private Long reviewId;
    private String profileUrl;
    private String restaurantName;
    private double latitude;
    private double longitude;
    private int orderingWaitingTime;

    public OrderPreviewWithRestSimpleDto(Order order) {
        super(order);
        this.restaurantId = order.getRestaurant().getId();
        this.reviewId = order.getReview() == null ? null : order.getReview().getId();
        this.profileUrl = order.getRestaurant().getProfileImageUrl();
        this.restaurantName = order.getRestaurant().getRestaurantName();
        this.latitude = order.getRestaurant().getLocation().getY();
        this.longitude = order.getRestaurant().getLocation().getX();
        this.orderingWaitingTime = order.getRestaurant().getOrderingWaitingTime();
    }
}

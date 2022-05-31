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
public class PreviousHistoryDto extends OrderPreviewDto {

    private Long restaurantId;
    private String profileUrl;
    private String restaurantName;

    public PreviousHistoryDto(Order order) {
        super(order);
        this.restaurantId = order.getRestaurant().getId();
        this.profileUrl = order.getRestaurant().getProfileImageUrl();
        this.restaurantName = order.getRestaurant().getRestaurantName();
    }
}

package com.server.ordering.domain.dto.response;

import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@NoArgsConstructor(access = PROTECTED)
public class RestaurantPreviewWithDistanceDto extends RestaurantPreviewDto {

    private int distanceMeter;

    public RestaurantPreviewWithDistanceDto(Long restaurantId, String restaurantName, String profileImageUrl, String backgroundImageUrl, int distanceMeter) {
        super(restaurantId, restaurantName, profileImageUrl, backgroundImageUrl);
        this.distanceMeter = distanceMeter;
    }
}

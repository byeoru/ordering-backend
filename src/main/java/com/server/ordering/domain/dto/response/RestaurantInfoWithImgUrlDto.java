package com.server.ordering.domain.dto.response;

import com.server.ordering.domain.Restaurant;
import com.server.ordering.domain.dto.request.RestaurantInfoDto;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@NoArgsConstructor(access = PROTECTED)
public class RestaurantInfoWithImgUrlDto extends RestaurantInfoDto {

    private String profileImageUrl;
    private String backgroundImageUrl;

    public RestaurantInfoWithImgUrlDto(Restaurant restaurant) {
        super(restaurant.getRestaurantName(), restaurant.getOwnerName(), restaurant.getAddress(),
                restaurant.getTableCount(), restaurant.getFoodCategory(), restaurant.getRestaurantType());

        this.profileImageUrl = restaurant.getProfileImageUrl();
        this.backgroundImageUrl = restaurant.getBackgroundImageUrl();
    }
}

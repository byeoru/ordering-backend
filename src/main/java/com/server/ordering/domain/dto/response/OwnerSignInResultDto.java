package com.server.ordering.domain.dto.response;

import com.server.ordering.domain.Restaurant;
import com.server.ordering.domain.dto.request.RestaurantInfoDto;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@NoArgsConstructor(access = PROTECTED)
public class OwnerSignInResultDto extends RestaurantInfoDto {

    private Long ownerId;
    private Long restaurantId;

    public OwnerSignInResultDto(Long ownerId) {
        this.ownerId = ownerId;
    }

    public OwnerSignInResultDto(Long ownerId, Restaurant restaurant) {

        super(restaurant.getRestaurantName(), restaurant.getOwnerName(), restaurant.getAddress(),
                restaurant.getTableCount(), restaurant.getFoodCategory(), restaurant.getRestaurantType());

        this.ownerId = ownerId;
        this.restaurantId = restaurant.getId();
    }
}

package com.server.ordering.domain.dto.request;

import com.server.ordering.domain.FoodCategory;
import com.server.ordering.domain.RestaurantType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class RestaurantDataWithLocationDto extends RestaurantDataDto {

    private double latitude;
    private double longitude;

    public RestaurantDataWithLocationDto(String restaurantName, String ownerName, String address, int tableCount,
                                         FoodCategory foodCategory, RestaurantType restaurantType, Integer admissionWaitingTime,
                                         Integer orderingWaitingTime, double latitude, double longitude) {
        super(restaurantName, ownerName, address, tableCount, foodCategory, restaurantType, admissionWaitingTime, orderingWaitingTime);
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

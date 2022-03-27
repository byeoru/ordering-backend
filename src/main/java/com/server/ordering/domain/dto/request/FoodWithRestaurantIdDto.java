package com.server.ordering.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class FoodWithRestaurantIdDto {

    private Long restaurantId;
    private String foodName;
    private int price;
    private boolean soldOut;
}

package com.server.ordering.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class RecentOrderRestaurantDto {

    private String restaurantName;
    private String backgroundImageUrl;
    private float rating;
    private int orderingWaitingTime;
}

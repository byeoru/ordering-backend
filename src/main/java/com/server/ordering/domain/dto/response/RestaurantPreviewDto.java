package com.server.ordering.domain.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@RequiredArgsConstructor
public class RestaurantPreviewDto {

    protected Long restaurantId;
    // private Integer ratings;
    protected String restaurantName;
    protected String profileImageUrl;
    protected String backgroundImageUrl;
    protected List<String> representativeMenus = new ArrayList<>();

    public void addRepresentativeFoodName(String foodName) {
        representativeMenus.add(foodName);
    }

    public RestaurantPreviewDto(Long restaurantId, String restaurantName, String profileImageUrl, String backgroundImageUrl, List<String> representativeMenus) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.profileImageUrl = profileImageUrl;
        this.backgroundImageUrl = backgroundImageUrl;
        this.representativeMenus = representativeMenus;
    }

    public RestaurantPreviewDto(Long restaurantId, String restaurantName, String profileImageUrl, String backgroundImageUrl) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.profileImageUrl = profileImageUrl;
        this.backgroundImageUrl = backgroundImageUrl;
    }
}

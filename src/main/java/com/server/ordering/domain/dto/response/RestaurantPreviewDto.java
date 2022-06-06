package com.server.ordering.domain.dto.response;

import com.server.ordering.domain.RepresentativeMenu;
import com.server.ordering.domain.Restaurant;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.*;

@Getter @Setter
@NoArgsConstructor(access = PROTECTED)
public class RestaurantPreviewDto {

    protected Long restaurantId;
    protected String restaurantName;
    protected String profileImageUrl;
    protected String backgroundImageUrl;
    protected List<String> representativeMenus = new ArrayList<>();

    public void addRepresentativeFoodName(String foodName) {
        representativeMenus.add(foodName);
    }

    public RestaurantPreviewDto(Restaurant restaurant) {
        this.restaurantId = restaurant.getId();
        this.restaurantName = restaurant.getRestaurantName();
        this.profileImageUrl = restaurant.getProfileImageUrl();
        this.backgroundImageUrl = restaurant.getBackgroundImageUrl();
        this.representativeMenus = restaurant.getRepresentativeMenus()
                .stream().map(RepresentativeMenu::getFoodName).collect(Collectors.toList());
    }

    public RestaurantPreviewDto(Long restaurantId, String restaurantName, String profileImageUrl, String backgroundImageUrl) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.profileImageUrl = profileImageUrl;
        this.backgroundImageUrl = backgroundImageUrl;
    }
}

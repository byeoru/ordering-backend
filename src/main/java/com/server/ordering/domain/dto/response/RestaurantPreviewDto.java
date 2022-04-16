package com.server.ordering.domain.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class RestaurantPreviewDto {

    private Long restaurantId;
    // private Integer ratings;
    private String restaurantName;
    private String profileImageUrl;
    private List<String> representativeMenus;
}

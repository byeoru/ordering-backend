package com.server.ordering.domain.dto.request;

import com.server.ordering.domain.FoodCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class RestaurantPreviewListReqDto {

    private double latitude; // 고객의 현재 위도
    private double longitude;// 고객의 현재 경도
    private FoodCategory foodCategory; // 음식 카테고리
}

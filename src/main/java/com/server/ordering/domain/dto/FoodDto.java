package com.server.ordering.domain.dto;

import com.server.ordering.domain.Food;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class FoodDto {

    private Long foodId;
    private String foodName;
    private int price;
    private boolean soldOut;
    private String imageUrl;

    public FoodDto(Food food) {
        this.foodId = food.getId();
        this.foodName = food.getFoodName();
        this.price = food.getPrice();
        this.soldOut = food.isSoldOut();
        this.imageUrl = food.getImageUrl();
    }
}

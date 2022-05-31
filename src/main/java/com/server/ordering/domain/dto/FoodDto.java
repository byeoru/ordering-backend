package com.server.ordering.domain.dto;

import com.server.ordering.domain.Food;
import com.server.ordering.domain.RepresentativeMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PUBLIC;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
public class FoodDto {

    // request 전용
    private String foodName;
    private int price;
    private String menuIntro;

    private Long foodId;
    private String imageUrl;
    private boolean soldOut;

    public FoodDto(Food food) {
        this.foodId = food.getId();
        this.foodName = food.getFoodName();
        this.price = food.getPrice();
        this.soldOut = food.isSoldOut();
        this.imageUrl = food.getImageUrl();
        this.menuIntro = food.getMenuIntro();
    }

    public FoodDto(RepresentativeMenu menu) {
        this.foodId = menu.getFood().getId();
        this.foodName = menu.getFood().getFoodName();
        this.price = menu.getFood().getPrice();
        this.soldOut = menu.getFood().isSoldOut();
        this.imageUrl = menu.getFood().getImageUrl();
        this.menuIntro = menu.getFood().getMenuIntro();
    }
}

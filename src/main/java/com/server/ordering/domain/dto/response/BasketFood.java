package com.server.ordering.domain.dto.response;

import com.server.ordering.domain.Basket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class BasketFood {

    private Long basketId;
    private Long foodId;
    private String foodName;
    private String imageUrl;
    private Boolean soldOut;
    private int price;
    private int count;

    public BasketFood(Basket basket) {
        this.basketId = basket.getId();
        this.foodId = basket.getFood().getId();
        this.foodName = basket.getFood().getFoodName();
        this.imageUrl = basket.getFood().getImageUrl();
        this.soldOut = basket.getFood().isSoldOut();
        this.price = basket.getFood().getPrice();
        this.count = basket.getCount();
    }
}

package com.server.ordering.domain.dto.response;

import com.server.ordering.domain.OrderFood;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class OrderFoodDto {

    private String foodName;
    private int price;
    private int count;

    public OrderFoodDto(OrderFood orderFood) {
        this.foodName = orderFood.getFood().getFoodName();
        this.price = orderFood.getPrice();
        this.count = orderFood.getCount();
    }
}

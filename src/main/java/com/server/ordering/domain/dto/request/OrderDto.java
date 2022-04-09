package com.server.ordering.domain.dto.request;

import com.server.ordering.domain.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class OrderDto {

    private Integer tableNumber;
    private int totalPrice;
    private OrderType orderType;
    private List<OrderFoodDto> orderFoodDtos;
}

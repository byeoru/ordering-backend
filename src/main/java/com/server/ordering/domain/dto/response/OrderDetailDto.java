package com.server.ordering.domain.dto.response;

import com.server.ordering.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class OrderDetailDto extends OrderPreviewWithRestSimpleDto {

    List<OrderFoodDto> orderFoods;

    public OrderDetailDto(Order order) {
        super(order);
        this.orderFoods = order.getOrderFoods().stream().map(OrderFoodDto::new).collect(Collectors.toList());
    }
}

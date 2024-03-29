package com.server.ordering.domain.dto.request;

import com.server.ordering.domain.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class OrderDto {

    private int tableNumber;
    private OrderType orderType;
}

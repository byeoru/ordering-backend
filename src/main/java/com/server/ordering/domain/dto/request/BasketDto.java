package com.server.ordering.domain.dto.request;

import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class BasketDto {

    private Long foodId;
    private int price;
    private int count;
}

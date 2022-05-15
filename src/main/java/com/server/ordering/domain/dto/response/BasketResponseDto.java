package com.server.ordering.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class BasketResponseDto {

    private Long basketId;
    private String foodName;
    private String imageUrl;
    private int price;
    private int count;
}

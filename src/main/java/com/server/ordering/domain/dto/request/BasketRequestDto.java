package com.server.ordering.domain.dto.request;

import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class BasketRequestDto {

    private Long foodId;
    private int count;
}

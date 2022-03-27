package com.server.ordering.domain.dto;

import lombok.*;

import static lombok.AccessLevel.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ResultDto<T> {

    private int size;
    private T data;
}

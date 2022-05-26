package com.server.ordering.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class SalesRequestDto {

    private String requestDateFormat; // 조회하고 싶은 YYYY 또는 YYYY-mm
    // ex) 2022년도 1~12달의 월별 매출 조회: 2022, 2022년도 5월달의 일별 매출 조회: 2022-05
}

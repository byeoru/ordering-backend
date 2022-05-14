package com.server.ordering.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class MyWaitingInfoDto {

    private Long waitingId;
    private Integer myWaitingNumber;
    private Long numInFrontOfMe;
}

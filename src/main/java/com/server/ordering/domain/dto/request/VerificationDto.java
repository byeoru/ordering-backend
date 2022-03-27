package com.server.ordering.domain.dto.request;

import lombok.*;

import static lombok.AccessLevel.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class VerificationDto {

    private String totalNum;
    private String code;
}

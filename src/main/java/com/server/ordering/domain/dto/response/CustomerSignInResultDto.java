package com.server.ordering.domain.dto.response;

import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class CustomerSignInResultDto {

    private Long customerId;
    private String nickname;
}

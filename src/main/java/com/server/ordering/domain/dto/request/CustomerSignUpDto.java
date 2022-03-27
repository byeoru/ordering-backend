package com.server.ordering.domain.dto.request;

import lombok.*;

import static lombok.AccessLevel.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class CustomerSignUpDto {

    private String nickname;
    private String signInId;
    private String password;
    private String phoneNumber;
}

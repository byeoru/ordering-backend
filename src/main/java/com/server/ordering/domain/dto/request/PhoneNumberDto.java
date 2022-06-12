package com.server.ordering.domain.dto.request;

import lombok.*;

import static lombok.AccessLevel.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class PhoneNumberDto {

    private String phoneNumber;
}

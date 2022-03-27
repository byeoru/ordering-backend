package com.server.ordering.domain.dto.request;

import lombok.*;

import static lombok.AccessLevel.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class PhoneNumberDto {

    private String phoneNumber;
    // 만약에 기존 번호 사용자가 탈퇴를 안하고 번호를 바꾼다면
    // 기존 번호를 물려받고 이 앱을 사용하려는 사람은 가입 불가??
}

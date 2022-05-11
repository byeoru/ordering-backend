package com.server.ordering.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@RequiredArgsConstructor
public class PhoneNumber {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "phone_number_id")
    private Long id;

    @NonNull
    private String phoneNumber;

    @NonNull
    @Enumerated(value = EnumType.STRING)
    private MemberType memberType;

    public void putPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

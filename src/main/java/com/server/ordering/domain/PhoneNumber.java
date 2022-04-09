package com.server.ordering.domain;

import lombok.*;

import javax.persistence.*;

import static lombok.AccessLevel.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = PROTECTED)
@RequiredArgsConstructor
public class PhoneNumber {

    @Id
    @Column(name = "phone_number")
    @NonNull
    private String phoneNumber;

    @NonNull
    @Enumerated(value = EnumType.STRING)
    private MemberType memberType;
}

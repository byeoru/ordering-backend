package com.server.ordering.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static lombok.AccessLevel.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = PROTECTED)
public class PhoneNumber {

    @Id
    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private MemberType memberType;

    public PhoneNumber(String phoneNumber, MemberType memberType) {
        this.phoneNumber = phoneNumber;
        this.memberType = memberType;
    }
}

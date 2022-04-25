package com.server.ordering.domain.member;

import com.server.ordering.domain.MyCoupon;
import com.server.ordering.domain.Order;
import com.server.ordering.domain.PhoneNumber;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Customer extends MemberBase {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    private String nickname;

    @OneToMany(mappedBy = "customer", cascade = REMOVE, orphanRemoval = true)
    private final List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = REMOVE, orphanRemoval = true)
    private final List<MyCoupon> coupons = new ArrayList<>();

    public Customer(String nickname, String signInId, String password, PhoneNumber phoneNumber) {
        this.nickname = nickname;
        this.signInId = signInId;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}

package com.server.ordering.domain.member;

import com.server.ordering.domain.*;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
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

    private Long basketKey;
    private String firebaseToken;

    @OneToMany(mappedBy = "customer", cascade = REMOVE, orphanRemoval = true)
    private final List<Basket> baskets = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = REMOVE, orphanRemoval = true)
    private final List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = REMOVE, orphanRemoval = true)
    private final List<MyCoupon> coupons = new ArrayList<>();

    @OneToOne(mappedBy = "customer", cascade = REMOVE, orphanRemoval = true, fetch = LAZY)
    private Waiting waiting;

    public Customer(String nickname, String signInId, String password, PhoneNumber phoneNumber) {
        super(signInId, password, phoneNumber);
        this.nickname = nickname;
    }

    public void changeBasketKey(Long restaurantId) {
        this.basketKey = restaurantId;
    }

    public Boolean isAbleToWaiting() {
        return this.waiting == null;
    }

    public void putFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public void clearFirebaseToken() {
        this.firebaseToken = null;
    }
}

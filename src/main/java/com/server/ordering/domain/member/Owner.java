package com.server.ordering.domain.member;

import com.server.ordering.domain.PhoneNumber;
import com.server.ordering.domain.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Owner extends MemberBase {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "owner_id")
    private Long id;

    @OneToOne(fetch = LAZY, cascade = REMOVE, orphanRemoval = true)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public Owner(String signInId, String password, PhoneNumber phoneNumber) {
        super(signInId, password, phoneNumber);
    }

    public void registerRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}

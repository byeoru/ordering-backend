package com.server.ordering.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@RequiredArgsConstructor
public class RepresentativeMenu {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "representative_menu_id")
    private Long id;

    @NonNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @NonNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    @NonNull
    private String foodName;

    public void registerRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}

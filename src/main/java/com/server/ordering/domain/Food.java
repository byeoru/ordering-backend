package com.server.ordering.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Food {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "food_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private String foodName;
    private int price;
    private boolean soldOut;
    private String imageUrl;
    private String menuIntro;

    public Food(String foodName, int price, boolean soldOut, String imageUrl, String menuIntro) {
        this.foodName = foodName;
        this.price = price;
        this.soldOut = soldOut;
        this.imageUrl = imageUrl;
        this.menuIntro = menuIntro;
    }

    public void changeSoldOutStatus(Boolean soldOut) {
        this.soldOut = soldOut;
    }

    public void registerRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void putFood(String foodName, int price, String menuIntro) {
        this.foodName = foodName;
        this.price = price;
        this.menuIntro = menuIntro;
    }

    public void putFood(String foodName, int price, String menuIntro, String imageUrl) {
        this.foodName = foodName;
        this.price = price;
        this.menuIntro = menuIntro;
        this.imageUrl = imageUrl;
    }
}

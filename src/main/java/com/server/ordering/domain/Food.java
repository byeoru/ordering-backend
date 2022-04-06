package com.server.ordering.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Food {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "food_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "food", cascade = REMOVE, orphanRemoval = true)
    private final List<Review> reviews = new ArrayList<>();

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
}

package com.server.ordering.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = PROTECTED)
public class Restaurant {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    private String restaurantName;

    private String ownerName;

    @OneToMany(mappedBy = "restaurant", cascade = REMOVE, orphanRemoval = true)
    private final List<Food> foods = new ArrayList<>();

    @Column(name = "restaurant_address")
    private String address;

    @Embedded
    private Location location;

    private int tableCount;

    @Enumerated(value = EnumType.STRING)
    private FoodCategory foodCategory;

    @Enumerated(value = EnumType.STRING)
    private RestaurantType restaurantType;

    private String imageUrl;

    public Restaurant(String restaurantName, String ownerName, String address,
                      int tableCount, FoodCategory foodCategory, RestaurantType restaurantType) {
        this.restaurantName = restaurantName;
        this.ownerName = ownerName;
        this.address = address;
        this.tableCount = tableCount;
        this.foodCategory = foodCategory;
        this.restaurantType = restaurantType;
    }

    public void addFood(Food food) {
        this.foods.add(food);
        food.setRestaurant(this);
    }
}

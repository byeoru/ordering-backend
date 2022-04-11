package com.server.ordering.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = PROTECTED)
@RequiredArgsConstructor
public class Restaurant {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @NonNull
    private String restaurantName;

    @NonNull
    private String ownerName;

    @OneToMany(mappedBy = "restaurant", cascade = REMOVE, orphanRemoval = true)
    private List<Food> foods = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Order> orders = new ArrayList<>();

    @NonNull
    @Column(name = "restaurant_address")
    private String address;

    @Embedded
    private Location location;

    @NonNull
    private int tableCount;

    @NonNull
    @Enumerated(value = EnumType.STRING)
    private FoodCategory foodCategory;

    @NonNull
    @Enumerated(value = EnumType.STRING)
    private RestaurantType restaurantType;

    private String imageUrl;

    public void addFood(Food food) {
        this.foods.add(food);
        food.registerRestaurant(this);
    }
}

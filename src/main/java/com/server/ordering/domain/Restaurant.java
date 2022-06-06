package com.server.ordering.domain;

import com.server.ordering.domain.member.Owner;
import lombok.*;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
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

    @OneToOne(mappedBy = "restaurant", fetch = LAZY)
    private Owner owner;

    @OneToMany(mappedBy = "restaurant", orphanRemoval = true)
    private List<Food> foods = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = REMOVE, orphanRemoval = true)
    private List<RepresentativeMenu> representativeMenus = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = REMOVE, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = REMOVE, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @NonNull
    @Column(name = "restaurant_address")
    private String address;

    @NonNull
    private Point location;

    @NonNull
    private Integer tableCount;

    @Version
    private int waitingCount;
    private int orderCount;

    @NonNull
    @Enumerated(value = EnumType.STRING)
    private FoodCategory foodCategory;

    @NonNull
    @Enumerated(value = EnumType.STRING)
    private RestaurantType restaurantType;

    private String profileImageUrl;
    private String backgroundImageUrl;
    private String notice;

    @NonNull
    private Integer orderingWaitingTime;
    @NonNull
    private Integer admissionWaitingTime;

    public void addFood(Food food) {
        this.foods.add(food);
        food.registerRestaurant(this);
    }

    public void putRestaurant(String restaurantName, String ownerName, String address,
                              int tableCount, FoodCategory foodCategory, RestaurantType restaurantType) {
        this.restaurantName = restaurantName;
        this.ownerName = ownerName;
        this.address = address;
        this.tableCount = tableCount;
        this.foodCategory = foodCategory;
        this.restaurantType = restaurantType;
    }

    public void putProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void putBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public void putOrderWaitingTime(Integer minutes) { this.orderingWaitingTime = minutes; }

    public void putAdmissionWaitingTime(Integer minutes) { this.admissionWaitingTime = minutes; }

    public void addRepresentativeMenu(RepresentativeMenu menu) {
        representativeMenus.add(menu);
        menu.registerRestaurant(this);
    }

    public void registerReview(Review review) {
        this.reviews.add(review);
    }

    public void increaseWaitingCount() {
        this.waitingCount++;
    }

    public void increaseOrderCount() {
        this.orderCount++;
    }

    public void putNotice(String notice) {
        this.notice = notice;
    }
}

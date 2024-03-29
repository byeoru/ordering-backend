package com.server.ordering.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Review {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @NonNull
    private String review;

    @NonNull
    @Column(nullable = false)
    private Float rating;

    private String imageUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public void registerOrderAndRestaurant(Order order, Restaurant restaurant) {
        this.order = order;
        this.restaurant = restaurant;
        order.registerReview(this);
        restaurant.registerReview(this);
    }

    public void putReview(String text, float rating) {
        this.review = text;
        this.rating = rating;
    }

    public void registerImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

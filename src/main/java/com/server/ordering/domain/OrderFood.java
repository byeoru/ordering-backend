package com.server.ordering.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@RequiredArgsConstructor(access = PRIVATE)
public class OrderFood {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_food_id")
    private Long id;

    @NonNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @NonNull
    private int price;
    @NonNull
    private int count;

    public void registerOrder(Order order) {
        this.order = order;
    }

    public static OrderFood createOrderFood(Food food, int price, int count) {
        return new OrderFood(food, price, count);
    }
}

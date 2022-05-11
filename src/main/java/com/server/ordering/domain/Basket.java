package com.server.ordering.domain;

import com.server.ordering.domain.member.Customer;
import lombok.*;
import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Basket {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "basket_id")
    private Long id;

    @NonNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NonNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    @NonNull
    private int price;
    @NonNull
    private int count;

    public static Basket CreateBasket(Customer customer, Food food, int price, int count) {
        return new Basket(customer, food, price, count);
    }
}

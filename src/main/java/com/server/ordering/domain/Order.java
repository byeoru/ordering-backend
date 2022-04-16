package com.server.ordering.domain;

import com.server.ordering.domain.member.Customer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.server.ordering.domain.OrderStatus.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter @Setter
@Table(name = "orders")
@NoArgsConstructor(access = PROTECTED)
@RequiredArgsConstructor(access = PUBLIC)
public class Order {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @NonNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = ALL, orphanRemoval = true)
    private final List<OrderFood> orderFoods = new ArrayList<>();

    @NonNull
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @OneToOne(mappedBy = "order")
    private Review review;

    @NonNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @NonNull
    @Enumerated(value = STRING)
    private OrderStatus status;

    @NonNull
    @Enumerated(value = STRING)
    private OrderType orderType;
    @NonNull
    private Integer tableNumber;
    @NonNull
    private int totalPrice;

    public void addOrderFood(OrderFood orderFood) {
        this.orderFoods.add(orderFood);
        orderFood.registerOrder(this);
    }

    public static Order createOrder(Customer customer, Restaurant restaurant, List<OrderFood> orderFoods,
                                    OrderType orderType, Integer tableNumber, int totalPrice) {
        Order order = new Order(customer, LocalDateTime.now(), restaurant, ORDERED, orderType, tableNumber, totalPrice);
        orderFoods.forEach(order::addOrderFood);
        return order;
    }

    public Boolean isAbleToCancel() {
        return this.status == ORDERED;
    }

    public Boolean isAbleToCompleted() {
        return this.status == ORDERED;
    }

    public void cancel() {
        this.status = CANCEL;
    }

    public void registerReview(Review review) {
        this.review = review;
    }

    public Boolean isAbleRegisterReview() {
        return Objects.isNull(this.review);
    }
}

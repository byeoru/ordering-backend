package com.server.ordering.domain;

import com.server.ordering.domain.member.Customer;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.*;

import static com.server.ordering.domain.OrderStatus.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter @Setter
@Table(name = "orders")
@RequiredArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Order {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @NonNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = ALL, orphanRemoval = true)
    private final Set<OrderFood> orderFoods = new HashSet<>();

    private ZonedDateTime receivedTime;
    private ZonedDateTime checkedTime;
    private ZonedDateTime canceledOrCompletedTime;

    private Integer myOrderNumber;

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
    @Column(nullable = false)
    private Integer totalPrice;
    @NonNull
    private String orderSummary;

    public void addOrderFood(OrderFood orderFood) {
        this.orderFoods.add(orderFood);
        orderFood.registerOrder(this);
    }

    public static Order createOrder(Customer customer, Restaurant restaurant, List<OrderFood> orderFoods,
                                    OrderType orderType, Integer tableNumber, int totalPrice, String orderSummary) {
        Order order = new Order(customer, restaurant, ORDERED, orderType, tableNumber, totalPrice, orderSummary);
        orderFoods.forEach(order::addOrderFood);
        return order;
    }

    public Boolean isAbleToCancel() {
        return this.status == ORDERED;
    }

    public Boolean isAbleToOwnerCancel() {
        return this.status == ORDERED || this.status == CHECKED;
    }

    public Boolean isAbleToCheck() {
        return this.status == ORDERED;
    }

    public Boolean isAbleToComplete() {
        return this.status == CHECKED;
    }

    public void registerReview(Review review) {
        this.review = review;
    }

    public Boolean isAbleRegisterReview() {
        return Objects.isNull(this.review);
    }

    public void registerReceivedTime() {
        this.receivedTime = ZonedDateTime.now();
    }

    public void registerCheckedTime() { this.checkedTime = ZonedDateTime.now(); }

    public void registerCanceledTime() {
        this.canceledOrCompletedTime = ZonedDateTime.now();
    }

    public void registerCompletedTime() {
        this.canceledOrCompletedTime = ZonedDateTime.now();
    }

    public void cancel() {
        this.status = CANCELED;
    }

    public void check() { this.status = CHECKED; }

    public void complete() {
        this.status = COMPLETED;
    }

    public void removeAllOrderFood() {
        orderFoods.clear();
    }

    public void registerMyOrderNumber(int orderNumber) {
        this.myOrderNumber = orderNumber;
    }
}

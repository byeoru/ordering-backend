package com.server.ordering.domain;

import com.server.ordering.domain.Order.Order;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter @Setter
public class OrderFood {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_food_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int totalPrice;
    private int count;
}

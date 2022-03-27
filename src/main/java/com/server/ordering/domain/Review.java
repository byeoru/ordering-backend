package com.server.ordering.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter @Setter
public class Review {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String review;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "food_id")
    private Food food;
}

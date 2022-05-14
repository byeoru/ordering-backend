package com.server.ordering.domain;

import com.server.ordering.domain.member.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Waiting {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "waiting_id")
    private Long id;

    @NonNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @NonNull
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NonNull
    private Integer myWaitingNumber;
    @NonNull
    private Byte peopleNumber;
}

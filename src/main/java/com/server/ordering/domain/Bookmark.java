package com.server.ordering.domain;

import com.server.ordering.domain.member.Customer;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Bookmark {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @NonNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @NonNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}

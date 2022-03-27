package com.server.ordering.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static javax.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Sales {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private int year;
    private byte month;
    private String sales;
}
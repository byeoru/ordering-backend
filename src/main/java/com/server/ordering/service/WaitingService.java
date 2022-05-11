package com.server.ordering.service;

import com.server.ordering.domain.Restaurant;
import com.server.ordering.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class WaitingService {

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void addWaitingCnt(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        restaurant.addWaitingCnt();
    }
}

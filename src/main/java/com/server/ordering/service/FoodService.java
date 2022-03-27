package com.server.ordering.service;

import com.server.ordering.domain.Food;
import com.server.ordering.repository.FoodRepository;
import com.server.ordering.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FoodService {

    private final FoodRepository foodRepository;

    public List<Food> getAllFood(Long restaurantId) {
        return foodRepository.findAll(restaurantId);
    }
}

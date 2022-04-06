package com.server.ordering.service;

import com.server.ordering.domain.Food;
import com.server.ordering.domain.Restaurant;
import com.server.ordering.domain.dto.FoodDto;
import com.server.ordering.repository.FoodRepository;
import com.server.ordering.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FoodService {

    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;

    public Food findFood(Long foodId) throws PersistenceException {
        return foodRepository.findOne(foodId);
    }

    @Transactional
    public Optional<Long> registerFood(Long restaurantId, FoodDto dto) throws PersistenceException {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        Food food = new Food(dto.getFoodName(), dto.getPrice(), dto.isSoldOut(), dto.getImageUrl(), dto.getMenuIntro());
        foodRepository.save(food);
        restaurant.addFood(food);
        return Optional.of(food.getId());
    }

    @Transactional
    public void putFood(Long foodId, FoodDto dto, Boolean bPutImage) {
        if (bPutImage) {
            foodRepository.putFood(foodId, dto.getFoodName(), dto.getPrice(),
                    dto.isSoldOut(), dto.getImageUrl(), dto.getMenuIntro());
        } else {
            foodRepository.putFood(foodId, dto.getFoodName(), dto.getPrice(),
                    dto.isSoldOut(), dto.getMenuIntro());
        }
    }

    public List<Food> getAllFood(Long restaurantId) {
        return foodRepository.findAll(restaurantId);
    }

    @Transactional
    public void deleteFood(Food food) {
        foodRepository.remove(food);
    }

    @Transactional
    public void delete(Long foodId) {
        foodRepository.remove(foodId);
    }
}

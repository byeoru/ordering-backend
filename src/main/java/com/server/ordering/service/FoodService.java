package com.server.ordering.service;

import com.server.ordering.S3Service;
import com.server.ordering.domain.Food;
import com.server.ordering.domain.Restaurant;
import com.server.ordering.domain.dto.FoodDto;
import com.server.ordering.domain.dto.request.FoodStatusDto;
import com.server.ordering.repository.FoodRepository;
import com.server.ordering.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FoodService {

    private final S3Service s3Service;
    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;

    public Food findFood(Long foodId) throws PersistenceException {
        return foodRepository.findOne(foodId);
    }

    @Transactional
    public Long registerFood(Long restaurantId, FoodDto dto, MultipartFile image) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        if (image != null) {
            String newImageKey = restaurantId + "food-image" + System.currentTimeMillis();
            String imageUrl = s3Service.upload(image, newImageKey);
            dto.setImageUrl(imageUrl);
        }
        Food food = new Food(dto.getFoodName(), dto.getPrice(), false, dto.getImageUrl(), dto.getMenuIntro());
        restaurant.addFood(food);
        foodRepository.save(food);
        return food.getId();
    }

    @Transactional
    public void putFood(Long foodId, Long restaurantId, FoodDto dto, MultipartFile image) {

        Food food = foodRepository.findOne(foodId);
        if (image != null) {

            if (food.getImageUrl() != null) {
                String imageUrl = food.getImageUrl();
                String imageKey = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

                // 기존 이미지 삭제
                s3Service.delete(imageKey);
            }
            String newImageKey = restaurantId +"food-image" + System.currentTimeMillis();

            // 이미지 S3 저장
            String newImageUrl = s3Service.upload(image, newImageKey);
            dto.setImageUrl(newImageUrl);
            food.putFood(dto.getFoodName(), dto.getPrice(), dto.getImageUrl(), dto.getMenuIntro());
        } else {
            food.putFood(dto.getFoodName(), dto.getPrice(), dto.getMenuIntro());
        }
    }

    @Transactional
    public void changeSoldOutStatus(Long foodId, FoodStatusDto dto) {
        Food food = foodRepository.findOne(foodId);
        food.changeSoldOutStatus(dto.getSoldOut());
    }

    public List<Food> getAllFood(Long restaurantId) {
        return foodRepository.findAll(restaurantId);
    }

    @Transactional
    public void deleteFood(Food food) {
        foodRepository.remove(food);
    }

    @Transactional
    public void deleteFood(Long foodId) {
        foodRepository.remove(foodId);
    }
}

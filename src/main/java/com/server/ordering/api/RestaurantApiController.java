package com.server.ordering.api;

import com.server.ordering.S3Service;
import com.server.ordering.domain.Food;
import com.server.ordering.domain.dto.request.RestaurantInfoDto;
import com.server.ordering.domain.dto.ResultDto;
import com.server.ordering.domain.dto.FoodDto;
import com.server.ordering.service.FoodService;
import com.server.ordering.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RestaurantApiController {

    private final RestaurantService restaurantService;
    private final FoodService foodService;
    private final S3Service s3Service;

    /**
     *
     * 매장 정보 변경
     */
    @PutMapping("/api/restaurant/{restaurantId}")
    public ResultDto<Boolean> putRestaurant(
            @PathVariable Long restaurantId,
            @RequestBody RestaurantInfoDto dto) {
        restaurantService.putRestaurant(restaurantId, dto);
        return new ResultDto<>(1, true);
    }


    /**
     *
     * 매장 음식 추가
     */
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @PostMapping("/api/restaurant/{restaurantId}/food")
    public ResultDto<Optional<Long>> registerFood(
            @PathVariable Long restaurantId,
            @RequestPart MultipartFile image,
            @RequestPart FoodDto dto) {

        String newImageKey = restaurantId +"food-image" + System.currentTimeMillis();
        String imageUrl = s3Service.upload(image, newImageKey);
        dto.setImageUrl(imageUrl);
        Optional<Long> foodId = foodService.registerFood(restaurantId, dto);
        return new ResultDto<>(1, foodId);
    }

    /**
     *
     * 매장 음식 삭제
     */
    @DeleteMapping("/api/restaurant/food/{foodId}")
    public ResultDto<Boolean> removeFood(@PathVariable Long foodId) {
        Food food = foodService.findFood(foodId);

        String imageUrl = food.getImageUrl();
        System.out.println(imageUrl);
        if (imageUrl != null) {
            String imageKey = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            System.out.println(imageKey);
            // 기존 이미지 삭제
            s3Service.delete(imageKey);
        }

        foodService.deleteFood(food);
        return new ResultDto<>(1, true);
    }

    /**
     *
     * 매장 음식 정보 변경
     */
    @PutMapping("/api/restaurant/{restaurantId}/food/{foodId}")
    public ResultDto<Boolean> putFood(
            @PathVariable Long restaurantId,
            @PathVariable Long foodId,
            @RequestPart(required = false) MultipartFile image,
            @RequestPart FoodDto dto) {

        if (image.isEmpty()) {
            foodService.putFood(foodId, dto, false);
        } else {
            Food food = foodService.findFood(foodId);
            String imageUrl = food.getImageUrl();
            String imageKey = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            String newImageKey = restaurantId +"food-image" + System.currentTimeMillis();

            // 기존 이미지 삭제
            s3Service.delete(imageKey);
            // 이미지 S3 저장
            String newImageUrl = s3Service.upload(image, newImageKey);
            dto.setImageUrl(newImageUrl);
            //storeFoodImage(image, restaurantId, dto);

            foodService.putFood(foodId, dto, true);
        }
        return new ResultDto<>(1, true);
    }

    /**
     *
     * 매장 모든 메뉴 반환
     */
    @PostMapping("/api/restaurant/{restaurantId}/foods")
    public ResultDto<List<FoodDto>> getAllFood(@PathVariable Long restaurantId) {
        List<Food> foods = foodService.getAllFood(restaurantId);
        List<FoodDto> foodDtoList = foods.stream().map(FoodDto::new).collect(Collectors.toList());
        return new ResultDto<>(foods.size(), foodDtoList);
    }
}

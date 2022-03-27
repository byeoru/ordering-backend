package com.server.ordering.api;

import com.server.ordering.domain.Food;
import com.server.ordering.domain.dto.request.RestaurantInfoDto;
import com.server.ordering.domain.dto.ResultDto;
import com.server.ordering.domain.dto.FoodDto;
import com.server.ordering.service.FoodService;
import com.server.ordering.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RestaurantApiController {

    private final RestaurantService restaurantService;
    private final FoodService foodService;

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
    @PostMapping("/api/restaurant/{restaurantId}/food")
    public ResultDto<Boolean> registerFood(
            @PathVariable Long restaurantId,
            @RequestBody FoodDto dto) {
        Optional<Long> optionalId = restaurantService.registerFood(restaurantId, dto);
        return new ResultDto<>(1, true);
    }

//    @PutMapping("/api/restaurant/food")
//    public ResultDto<Boolean> putFood(@RequestBody )

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

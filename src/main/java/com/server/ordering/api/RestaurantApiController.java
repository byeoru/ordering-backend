package com.server.ordering.api;

import com.server.ordering.domain.*;
import com.server.ordering.domain.dto.request.*;
import com.server.ordering.domain.dto.ResultDto;
import com.server.ordering.domain.dto.FoodDto;
import com.server.ordering.domain.dto.response.*;
import com.server.ordering.service.FoodService;
import com.server.ordering.service.OrderService;
import com.server.ordering.service.RestaurantService;
import com.server.ordering.service.WaitingService;
import lombok.RequiredArgsConstructor;
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
    private final WaitingService waitingService;
    private final OrderService orderService;

    /**
     * 매장 정보 변경
     */
    @PutMapping("/api/restaurant/{restaurantId}")
    public ResultDto<Boolean> putRestaurant(@PathVariable Long restaurantId,
                                            @RequestBody RestaurantDataWithLocationDto dto) {
        restaurantService.putRestaurant(restaurantId, dto);
        return new ResultDto<>(1, true);
    }

    /**
     * 매장 음식 추가
     */
    @PostMapping("/api/restaurant/{restaurantId}/food")
    public ResultDto<Long> registerFood(@PathVariable Long restaurantId,
                                        @RequestPart(required = false) MultipartFile image,
                                        @RequestPart FoodDto dto) {

        Long foodId = foodService.registerFood(restaurantId, dto, image);
        return new ResultDto<>(1, foodId);
    }

    /**
     * 매장 음식 삭제
     */
    @DeleteMapping("/api/restaurant/food/{foodId}")
    public ResultDto<Boolean> removeFood(@PathVariable Long foodId) {
        Food food = foodService.findFood(foodId);

        String imageUrl = food.getImageUrl();
        if (imageUrl != null) {
            String imageKey = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            // 기존 이미지 삭제
            //s3Service.delete(imageKey);
        }

        foodService.deleteFood(food);
        return new ResultDto<>(1, true);
    }

    /**
     * 매장 음식 정보 변경
     */
    @PutMapping("/api/restaurant/{restaurantId}/food/{foodId}")
    public ResultDto<Boolean> putFood(@PathVariable Long restaurantId,
                                      @PathVariable Long foodId,
                                      @RequestPart(required = false) MultipartFile image,
                                      @RequestPart FoodDto dto) {

        foodService.putFood(foodId, restaurantId, dto, image);
        return new ResultDto<>(1, true);
    }

    /**
     * 매장 음식 품절 정보 변경
     */
    @PutMapping("/api/restaurant/food/{foodId}/status")
    public ResultDto<Boolean> putFoodStatus(@PathVariable Long foodId,
                                            @RequestBody FoodStatusDto dto) {
        foodService.changeSoldOutStatus(foodId, dto);
        return new ResultDto<>(1, true);
    }

    /**
     * 매장 모든 메뉴 반환
     */
    @PostMapping("/api/restaurant/{restaurantId}/foods")
    public ResultDto<List<FoodDto>> getAllFood(@PathVariable Long restaurantId) {
        List<Food> foods = foodService.getAllFood(restaurantId);
        List<FoodDto> foodDtoList = foods.stream().map(FoodDto::new).collect(Collectors.toList());
        return new ResultDto<>(foods.size(), foodDtoList);
    }

    /**
     * 매장 한 달 매출 반환
     */
    @PostMapping("/api/restaurant/{restaurantId}/daily_sales")
    public ResultDto<List<SalesResponseDto>> getDailySales(@PathVariable Long restaurantId,
                                                           @RequestBody SalesRequestDto dto) {
        List<SalesResponseDto> sales = restaurantService.getDailySalesOfRestaurant(restaurantId, dto.getRequestDateFormat());
        return new ResultDto<>(sales.size(), sales);
    }

    /**
     * 특정 연도의 매장 월별 매출 반환
     */
    @PostMapping("/api/restaurant/{restaurantId}/monthly_sales")
    public ResultDto<List<SalesResponseDto>> getMonthlySales(@PathVariable Long restaurantId,
                                                             @RequestBody SalesRequestDto dto) {
        List<SalesResponseDto> sales = restaurantService.getMonthlySalesOfRestaurant(restaurantId, dto.getRequestDateFormat());
        return new ResultDto<>(sales.size(), sales);
    }

    /**
     * 매장 프로필 이미지 등록, 변경
     */
    @PutMapping("/api/restaurant/{restaurantId}/profile_image")
    public ResultDto<Boolean> putProfileImage(@PathVariable Long restaurantId,
                                              @RequestPart MultipartFile image) {
        restaurantService.putRestaurantProfileImage(restaurantId, image);
        return new ResultDto<>(1, true);
    }

    /**
     * 매장 배경 이미지 등록, 변경
     */
    @PutMapping("/api/restaurant/{restaurantId}/background_image")
    public ResultDto<Boolean> putBackgroundImage(@PathVariable Long restaurantId,
                                                 @RequestPart MultipartFile image) {
        restaurantService.putRestaurantBackgroundImage(restaurantId, image);
        return new ResultDto<>(1, true);
    }

    /**
     * 매장 대표 음식 추가
     */
    @PostMapping("/api/restaurant/{restaurantId}/representative")
    public ResultDto<Boolean> addRepresentativeMenu(@PathVariable Long restaurantId,
                                                    @RequestParam(name = "food_id") Long foodId) {
        Boolean bExist = restaurantService.isExistRepresentativeMenu(restaurantId, foodId);
        if (bExist) {
            return new ResultDto<>(1, false);
        }
        Boolean result = restaurantService.addRepresentativeMenu(restaurantId, foodId);
        return new ResultDto<>(1, result);
    }

    /**
     * 매장 대표 음식 삭제
     */
    @DeleteMapping("/api/restaurant/representative/{representativeMenuId}")
    public ResultDto<Boolean> removeRepresentativeMenu(@PathVariable Long representativeMenuId) {
        restaurantService.removeRepresentativeMenu(representativeMenuId);
        return new ResultDto<>(1, true);
    }

    /**
     * 매장 대표 음식리스트 반환
     */
    @GetMapping("/api/restaurant/{restaurantId}/representatives")
    public ResultDto<List<RepresentativeMenuDto>> getRepresentatives(@PathVariable Long restaurantId) {
        List<RepresentativeMenu> representativeMenus = restaurantService.findAllRepresentative(restaurantId);
        List<RepresentativeMenuDto> representativeMenuDtos = representativeMenus
                .stream().map(RepresentativeMenuDto::new).collect(Collectors.toList());
        return new ResultDto<>(representativeMenuDtos.size(), representativeMenuDtos);
    }

    /**
     * 매장 preview data 반환
     */
    @PostMapping("/api/restaurant/{restaurantId}/preview")
    public ResultDto<RestaurantPreviewDto> getRestaurantPreview(@PathVariable Long restaurantId) {
        Restaurant restaurant = restaurantService.findRestaurant(restaurantId);
        RestaurantPreviewDto previewDto = new RestaurantPreviewDto(restaurant);

        return new ResultDto<>(1, previewDto);
    }

    /**
     * 현재 사용자 위치에서 반경 3km 이내의 매장 리스트를 반환
     */
    @PostMapping("/api/restaurants")
    public ResultDto<List<RestaurantPreviewWithDistanceDto>> getRestaurantList(@RequestBody RestaurantPreviewListReqDto dto) {
        List<RestaurantPreviewWithDistanceDto> previews = restaurantService.getAllForPreview(dto);
        return new ResultDto<>(previews.size(), previews);
    }

    /**
     * 매장 주문 예상 대기시간 설정/변경
     */
    @PutMapping("/api/restaurant/{restaurantId}/order_waiting_time")
    public ResultDto<Boolean> putOrderWaitingTime(@PathVariable Long restaurantId,
                                                  @RequestBody WaitingTimeDto dto) {
        restaurantService.putOrderWaitingTime(restaurantId, dto);
        return new ResultDto<>(1, true);
    }

    /**
     * 매장 입장 예상 대기시간 설정/변경
     */
    @PutMapping("/api/restaurant/{restaurantId}/admission_waiting_time")
    public ResultDto<Boolean> putAdmissionWaitingTime(@PathVariable Long restaurantId,
                                                      @RequestBody WaitingTimeDto dto) {
        restaurantService.putAdmissionWaitingTime(restaurantId, dto);
        return new ResultDto<>(1, true);
    }

    /**
     * 매장 웨이팅 리스트 반환
     */
    @PostMapping("/api/restaurant/{restaurantId}/waitings")
    public ResultDto<List<WaitingPreviewDto>> getWaitingList(@PathVariable Long restaurantId) {
        List<Waiting> waitings = waitingService.findAllWithPhoneNumberRestaurantWaiting(restaurantId);
        List<WaitingPreviewDto> waitingPreviewDtos = waitings.stream().map(WaitingPreviewDto::new).collect(Collectors.toList());
        return new ResultDto<>(waitingPreviewDtos.size(), waitingPreviewDtos);
    }

    /**
     * 진행중인 주문 리스트 반환(ORDERED, CHECKED)
     */
    @GetMapping("/api/restaurant/{restaurantId}/orders/ongoing")
    public ResultDto<List<OrderPreviewDto>> getOngoingOrderList(@PathVariable Long restaurantId) {

        List<Order> ongoingOrders = orderService.findOngoingOrders(restaurantId);
        List<OrderPreviewDto> previewDtos = ongoingOrders.stream().map(OrderPreviewDto::new).collect(Collectors.toList());
        return new ResultDto<>(previewDtos.size(), previewDtos);
    }

    /**
     * 완료된 주문 리스트 반환(CANCELED, COMPLETED)
     */
    @GetMapping("/api/restaurant/{restaurantId}/orders/finished")
    public ResultDto<List<OrderPreviewDto>> getFinishedOrderList(@PathVariable Long restaurantId) {

        List<Order> finishedOrders = orderService.findFinishedOrders(restaurantId);
        List<OrderPreviewDto> previewDtos = finishedOrders.stream().map(OrderPreviewDto::new).collect(Collectors.toList());
        return new ResultDto<>(previewDtos.size(), previewDtos);
    }

    /**
     * 매장 공지 작성/수정
     */
    @PutMapping("/api/restaurant/{restaurantId}/notice")
    public ResultDto<Boolean> putNotice(@PathVariable Long restaurantId,
                                        @RequestBody MessageDto dto) {
        restaurantService.putRestaurantNotice(restaurantId, dto);
        return new ResultDto<>(1, true);
    }

    /**
     * 매장 소개(공지, 좌표) 반환
     */
    @GetMapping("/api/restaurant/{restaurantId}/info")
    public ResultDto<RestaurantInfoDto> getRestaurantInfo(@PathVariable Long restaurantId) {
        Restaurant restaurant = restaurantService.findRestaurant(restaurantId);
        RestaurantInfoDto restaurantInfoDto = new RestaurantInfoDto(restaurant);
        return new ResultDto<>(1, restaurantInfoDto);
    }

    /**
     * 매장 리뷰 리스트 반환
     */
    @GetMapping("/api/restaurant/{restaurantId}/reviews")
    public ResultDto<List<ReviewPreviewDto>> getAllReview(@PathVariable Long restaurantId) {
        List<Review> reviews = restaurantService.findReviewWithOrderWithCustomer(restaurantId);
        List<ReviewPreviewDto> previewDtos = reviews.stream().map(ReviewPreviewDto::new).collect(Collectors.toList());
        return new ResultDto<>(previewDtos.size(), previewDtos);
    }

    /**
     * 웨이팅 호출
     */
    @DeleteMapping("/api/restaurant/waiting/{waitingId}/call")
    public ResultDto<Boolean> callWaiting(@PathVariable Long waitingId) {
        restaurantService.callWaiting(waitingId);
        waitingService.cancelOrDeleteWaiting(waitingId);
        return new ResultDto<>(1, true);
    }
}
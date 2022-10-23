package com.byeoru.ordering_server.api

import com.byeoru.ordering_server.domain.*
import com.byeoru.ordering_server.domain.dto.FoodDto
import com.byeoru.ordering_server.domain.dto.ResultDto
import com.byeoru.ordering_server.domain.dto.request.*
import com.byeoru.ordering_server.domain.dto.response.*
import com.byeoru.ordering_server.service.FoodService
import com.byeoru.ordering_server.service.OrderService
import com.byeoru.ordering_server.service.RestaurantService
import com.byeoru.ordering_server.service.WaitingService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class RestaurantApiController(private val restaurantService: RestaurantService,
                              private val foodService: FoodService,
                              private val waitingService: WaitingService,
                              private val orderService: OrderService) {

    /**
     * 매장 정보 변경
     */
    @PutMapping("/api/restaurant/{restaurantId}")
    fun putRestaurant(@PathVariable restaurantId: Long,
                      @RequestBody dto: RestaurantDataWithLocationDto): ResultDto<Boolean> {
        restaurantService.putRestaurant(restaurantId, dto)
        return ResultDto(1, true)
    }

    /**
     * 매장 음식 추가
     */
    @PostMapping("/api/restaurant/{restaurantId}/food")
    fun registerFood(@PathVariable restaurantId: Long,
                     @RequestPart(required = false) image: MultipartFile?,
                     @RequestPart dto: FoodDto): ResultDto<Long> {
        val foodId = foodService.registerFood(restaurantId, dto, image)
        return ResultDto(1, foodId)
    }

    /**
     * 매장 음식 삭제
     */
    @DeleteMapping("/api/restaurant/food/{foodId}")
    fun removeFood(@PathVariable foodId: Long): ResultDto<Boolean> {
        foodService.deleteFood(foodId)
        return ResultDto(1, true)
    }

    /**
     * 매장 음식 정보 변경
     */
    @PutMapping("/api/restaurant/{restaurantId}/food/{foodId}")
    fun putFood(@PathVariable restaurantId: Long,
                @PathVariable foodId: Long,
                @RequestPart(required = false) image: MultipartFile?,
                @RequestPart dto: FoodDto): ResultDto<Boolean> {
        foodService.putFood(foodId, restaurantId, dto, image)
        return ResultDto(1, true)
    }

    /**
     * 매장 음식 품절 정보 변경
     */
    @PutMapping("/api/restaurant/food/{foodId}/status")
    fun putFoodStatus(@PathVariable foodId: Long,
                      @RequestBody dto: FoodStatusDto): ResultDto<Boolean> {
        foodService.changeSoldOutStatus(foodId, dto)
        return ResultDto(1, true)
    }

    /**
     * 매장 모든 메뉴 반환
     */
    @PostMapping("/api/restaurant/{restaurantId}/foods")
    fun getAllFood(@PathVariable restaurantId: Long): ResultDto<List<FoodDto>> {
        val foods = foodService.getAllFood(restaurantId)
        val foodDtoList = foods.map { FoodDto(it) }
        return ResultDto(foods.size, foodDtoList)
    }

    /**
     * 매장 특정 달의 일별 매출 반환
     */
    @PostMapping("/api/restaurant/{restaurantId}/daily_sales")
    fun getDailySales(@PathVariable restaurantId: Long,
                      @RequestBody dto: SalesRequestDto): ResultDto<List<SalesResponseDto>> {
        val sales = restaurantService.getDailySalesOfRestaurant(restaurantId, dto.requestDateFormat)
        return ResultDto(sales.size, sales)
    }

    /**
     * 매장 특정 연도의 월별 매출 반환
     */
    @PostMapping("/api/restaurant/{restaurantId}/monthly_sales")
    fun getMonthlySales(@PathVariable restaurantId: Long,
                        @RequestBody dto: SalesRequestDto): ResultDto<List<SalesResponseDto>> {
        val sales = restaurantService.getMonthlySalesOfRestaurant(restaurantId, dto.requestDateFormat)
        return ResultDto(sales.size, sales)
    }

    /**
     * 매장 프로필 이미지 등록, 변경
     */
    @PutMapping("/api/restaurant/{restaurantId}/profile_image")
    fun putProfileImage(@PathVariable restaurantId: Long,
                        @RequestPart image: MultipartFile): ResultDto<Boolean> {
        restaurantService.putRestaurantProfileImage(restaurantId, image)
        return ResultDto(1, true)
    }

    /**
     * 매장 배경 이미지 등록, 변경
     */
    @PutMapping("/api/restaurant/{restaurantId}/background_image")
    fun putBackgroundImage(@PathVariable restaurantId: Long,
                           @RequestPart image: MultipartFile): ResultDto<Boolean> {
        restaurantService.putRestaurantBackgroundImage(restaurantId, image)
        return ResultDto(1, true)
    }

    /**
     * 매장 대표 음식 추가
     */
    @PostMapping("/api/restaurant/{restaurantId}/representative")
    fun addRepresentativeMenu(@PathVariable restaurantId: Long,
                              @RequestParam(name = "food_id") foodId: Long): ResultDto<Boolean> {
        val bExist = restaurantService.isExistRepresentativeMenu(foodId)
        return if (bExist) {
            ResultDto(1, false)
        } else {
            restaurantService.addRepresentativeMenu(restaurantId, foodId)
            ResultDto(1, true)
        }
    }

    /**
     * 매장 대표 음식 삭제
     */
    @DeleteMapping("/api/restaurant/representative/{representativeMenuId}")
    fun removeRepresentativeMenu(@PathVariable representativeMenuId: Long): ResultDto<Boolean> {
        restaurantService.removeRepresentativeMenu(representativeMenuId)
        return ResultDto(1, true)
    }

    /**
     * 매장 대표 음식리스트 반환
     */
    @GetMapping("/api/restaurant/{restaurantId}/representatives")
    fun getRepresentatives(@PathVariable restaurantId: Long): ResultDto<List<RepresentativeMenuDto>> {
        val representativeMenus = restaurantService.findAllRepresentative(restaurantId)
        val representativeMenuDtos = representativeMenus.map { RepresentativeMenuDto(it) }
        return ResultDto(representativeMenuDtos.size, representativeMenuDtos)
    }

    /**
     * 매장 preview data 반환
     */
    @PostMapping("/api/restaurant/{restaurantId}/preview")
    fun getRestaurantPreview(@PathVariable restaurantId: Long): ResultDto<RestaurantPreviewDto> {
        val restaurant = restaurantService.findRestaurant(restaurantId)
        val previewDto = RestaurantPreviewDto(restaurant)
        return ResultDto(1, previewDto)
    }

    /**
     * 현재 사용자 위치에서 반경 3km 이내의 매장 리스트를 반환
     */
    @PostMapping("/api/restaurants")
    fun getRestaurantList(@RequestBody dto: RestaurantPreviewListReqDto): ResultDto<List<RestaurantPreviewWithDistanceDto>> {
        val previews = restaurantService.getAllForPreview(dto)
        return ResultDto(previews.size, previews)
    }

    /**
     * 매장 주문 예상 대기시간 설정/변경
     */
    @PutMapping("/api/restaurant/{restaurantId}/order_waiting_time")
    fun putOrderWaitingTime(@PathVariable restaurantId: Long,
                            @RequestBody dto: WaitingTimeDto): ResultDto<Boolean> {
        restaurantService.putOrderWaitingTime(restaurantId, dto)
        return ResultDto(1, true)
    }

    /**
     * 매장 입장 예상 대기시간 설정/변경
     */
    @PutMapping("/api/restaurant/{restaurantId}/admission_waiting_time")
    fun putAdmissionWaitingTime(@PathVariable restaurantId: Long,
                                @RequestBody dto: WaitingTimeDto): ResultDto<Boolean> {
        restaurantService.putAdmissionWaitingTime(restaurantId, dto)
        return ResultDto(1, true)
    }

    /**
     * 매장 웨이팅 리스트 반환
     */
    @PostMapping("/api/restaurant/{restaurantId}/waitings")
    fun getWaitingList(@PathVariable restaurantId: Long): ResultDto<List<WaitingPreviewDto>> {
        val waitings = waitingService.findAllWithPhoneNumberRestaurantWaiting(restaurantId)
        val waitingPreviewDtos = waitings.map { WaitingPreviewDto(it) }
        return ResultDto(waitingPreviewDtos.size, waitingPreviewDtos)
    }

    /**
     * 진행중인 주문 리스트 반환(ORDERED, CHECKED)
     */
    @GetMapping("/api/restaurant/{restaurantId}/orders/ongoing")
    fun getOngoingOrderList(@PathVariable restaurantId: Long): ResultDto<List<OrderPreviewDto>> {
        val ongoingOrders = orderService.findOngoingOrders(restaurantId)
        val previewDtos = ongoingOrders.map { OrderPreviewDto(it) }
        return ResultDto(previewDtos.size, previewDtos)
    }

    /**
     * 완료된 주문 리스트 반환(CANCELED, COMPLETED)
     */
    @GetMapping("/api/restaurant/{restaurantId}/orders/finished")
    fun getFinishedOrderList(@PathVariable restaurantId: Long): ResultDto<List<OrderPreviewDto>> {
        val finishedOrders = orderService.findFinishedOrders(restaurantId)
        val previewDtos = finishedOrders.map { OrderPreviewDto(it) }
        return ResultDto(previewDtos.size, previewDtos)
    }

    /**
     * 매장 공지 작성/수정
     */
    @PutMapping("/api/restaurant/{restaurantId}/notice")
    fun putNotice(@PathVariable restaurantId: Long,
                  @RequestBody dto: MessageDto): ResultDto<Boolean> {
        restaurantService.putRestaurantNotice(restaurantId, dto)
        return ResultDto(1, true)
    }

    /**
     * 매장 소개(공지, 좌표) 반환
     */
    @GetMapping("/api/restaurant/{restaurantId}/info")
    fun getRestaurantInfo(@PathVariable restaurantId: Long): ResultDto<RestaurantInfoDto> {
        val restaurant = restaurantService.findRestaurant(restaurantId)
        val restaurantInfoDto = RestaurantInfoDto(restaurant)
        return ResultDto(1, restaurantInfoDto)
    }

    /**
     * 매장 리뷰 리스트 반환
     */
    @GetMapping("/api/restaurant/{restaurantId}/reviews")
    fun getAllReview(@PathVariable restaurantId: Long): ResultDto<List<ReviewPreviewDto>> {
        val reviews = restaurantService.findReviewWithOrderWithCustomer(restaurantId)
        val previewDtos = reviews.map { ReviewPreviewDto(it) }
        return ResultDto(previewDtos.size, previewDtos)
    }

    /**
     * 웨이팅 호출
     */
    @DeleteMapping("/api/restaurant/waiting/{waitingId}/call")
    fun callWaiting(@PathVariable waitingId: Long): ResultDto<Boolean> {
        restaurantService.callWaiting(waitingId)
        waitingService.cancelOrDeleteWaiting(waitingId)
        return ResultDto(1, true)
    }
}
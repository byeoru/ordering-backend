package com.server.ordering.api;

import com.server.ordering.domain.Basket;
import com.server.ordering.domain.dto.ResultDto;
import com.server.ordering.domain.dto.request.BasketPutDto;
import com.server.ordering.domain.dto.request.BasketRequestDto;
import com.server.ordering.domain.dto.request.OrderDto;
import com.server.ordering.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;

    /**
     * 장바구니에 추가
     */
    @PostMapping("/api/order/basket")
    public ResultDto<Boolean> addToBasket(@RequestParam(name = "customer_id") Long customerId,
                                          @RequestParam(name = "restaurant_id") Long restaurantId,
                                          @RequestBody BasketRequestDto dto) {

        // 이미 저장되이 있는 음식인지 조회
        Basket basket = orderService.findBasketByCustomerIdAndFoodId(customerId, dto.getFoodId());

        // 이미 저장된 음식일 경우
        if (basket != null) {
            orderService.updateBasketCount(basket, dto);
            return new ResultDto<>(1, true);
        }
        // 장바구니에 추가 가능한지 체크
        Boolean bAbleToAddToBasket = orderService.isAbleToAddToBasket(customerId, restaurantId);

        if (bAbleToAddToBasket) {
            orderService.addToBasket(customerId, restaurantId, dto);
            return new ResultDto<>(1, true);
        }
        return new ResultDto<>(1, false);
    }

    /**
     * 장바구니 음식(여러개) 수량 변경
     */
    @PutMapping("/api/order/baskets")
    public ResultDto<Boolean> putFoodCntInBasket(@RequestParam(name = "customer_id") Long customerId,
                                                 @RequestBody List<BasketPutDto> basketPutDtos) {

        orderService.putFoodCntInBasket(customerId, basketPutDtos);
        return new ResultDto<>(1, true);
    }

    /**
     * 장바구니에서 음식 삭제
     */
    @DeleteMapping("/api/order/basket/{basketId}")
    public ResultDto<Boolean> removeToBasket(@PathVariable Long basketId,
                                             @RequestParam(name = "customer_id") Long customerId) {
        orderService.removeToBasket(basketId, customerId);
        return new ResultDto<>(1, true);
    }


    /**
     * 장바구니 음식 주문
     */
    @PostMapping("/api/order")
    public ResultDto<Long> order(@RequestParam(name = "customer_id") Long customerId,
                                 @RequestBody OrderDto dto) {
        // 주문
        Long orderId = orderService.order(customerId, dto);
        // 장바구니 비우기
        orderService.removeAllToBasket(customerId);
        return new ResultDto<>(1, orderId);
    }

    /**
     * 고객 주문 취소
     */
    @PostMapping("/api/order/{orderId}/cancel")
    public ResultDto<Boolean> cancel(@PathVariable Long orderId) {
        Boolean bCanceled = orderService.orderCancel(orderId);
        return new ResultDto<>(1, bCanceled);
    }

    /**
     * 점주 주문 취소
     */
    @PostMapping("/api/order/{orderId}/owner_cancel")
    public ResultDto<Boolean> ownerCancel(@PathVariable Long orderId) {
        Boolean bCanceled = orderService.orderOwnerCancel(orderId);
        return new ResultDto<>(1, bCanceled);
    }

    /**
     * 주문 체크
     */
    @PostMapping("/api/order/{orderId}/check")
    public ResultDto<Boolean> check(@PathVariable Long orderId) {
        Boolean bChecked = orderService.orderCheck(orderId);
        return new ResultDto<>(1, bChecked);
    }

    /**
     * 주문 완료
     */
    @PostMapping("/api/order/{orderId}/complete")
    public ResultDto<Boolean> complete(@PathVariable Long orderId) {
        Boolean bCompleted = orderService.orderComplete(orderId);
        return new ResultDto<>(1, bCompleted);
    }
}
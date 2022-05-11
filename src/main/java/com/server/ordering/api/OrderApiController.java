package com.server.ordering.api;

import com.server.ordering.domain.dto.ResultDto;
import com.server.ordering.domain.dto.request.BasketDto;
import com.server.ordering.domain.dto.request.OrderDto;
import com.server.ordering.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
                                          @RequestBody BasketDto dto) {
        Boolean bAbleToAddToBasket = orderService.isAbleToAddToBasket(customerId, restaurantId);
        if (bAbleToAddToBasket) {
            orderService.addToBasket(customerId, restaurantId, dto.getFoodId(), dto);
            return new ResultDto<>(1, true);
        }
        return new ResultDto<>(1, false);
    }

    /**
     * 장바구니에서 제거
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
        Long orderId = orderService.order(customerId, dto);
        orderService.removeAllToBasket(customerId);
        return new ResultDto<>(1, orderId);
    }

    /**
     * 주문 취소
     */
    @PostMapping("/api/order/{orderId}/cancel")
    public ResultDto<Boolean> cancel(@PathVariable Long orderId) {
        Boolean isCanceled = orderService.orderCancel(orderId);
        return new ResultDto<>(1, isCanceled);
    }

    /**
     * 주문 확정
     */
    @PostMapping("/api/order/{orderId}/completed")
    public ResultDto<Boolean> completed(@PathVariable Long orderId) {
        Boolean isCompleted = orderService.setOrderToCompleted(orderId);
        return new ResultDto<>(1, isCompleted);
    }
}

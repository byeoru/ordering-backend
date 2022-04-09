package com.server.ordering.api;

import com.server.ordering.domain.dto.ResultDto;
import com.server.ordering.domain.dto.request.OrderDto;
import com.server.ordering.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;

    @PostMapping("/api/order")
    public ResultDto<Long> order(@RequestParam(name = "customer_id") Long customerId,
                                 @RequestParam(name = "restaurant_id") Long restaurantId,
                                 @RequestBody OrderDto dto) {
        Long orderId = orderService.order(customerId, restaurantId, dto);
        return new ResultDto<>(1, orderId);
    }

    @PostMapping("/api/order/{orderId}/cancel")
    public ResultDto<Boolean> cancel(@PathVariable Long orderId) {
        Boolean isCanceled = orderService.orderCancel(orderId);
        return new ResultDto<>(1, isCanceled);
    }
}

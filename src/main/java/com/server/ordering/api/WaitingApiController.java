package com.server.ordering.api;

import com.server.ordering.domain.dto.ResultDto;
import com.server.ordering.domain.dto.request.WaitingRegisterDto;
import com.server.ordering.domain.member.Customer;
import com.server.ordering.service.CustomerService;
import com.server.ordering.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WaitingApiController {

    private final WaitingService waitingService;
    private final CustomerService customerService;

    /**
     * 웨이팅 접수
     */
    @PostMapping("/api/waiting")
    public ResultDto<Boolean> registerWaiting(@RequestParam(name = "restaurant_id") Long restaurantId,
                                              @RequestParam(name = "customer_id") Long customerId,
                                              @RequestBody WaitingRegisterDto dto) {
        Customer customer = customerService.findCustomerWithWaiting(customerId);
        if (customer.isAbleToWaiting()) {
            waitingService.registerWaiting(restaurantId, customerId, dto.getPeopleNumber());
            return new ResultDto<>(1, true);
        }

        return new ResultDto<>(1, false);
    }

    /**
     * 웨이팅 취소
     */
    @DeleteMapping("/api/waiting/{waitingId}")
    public ResultDto<Boolean> cancelWaiting(@PathVariable Long waitingId) {
        waitingService.cancelWaiting(waitingId);
        return new ResultDto<>(1, true);
    }
}

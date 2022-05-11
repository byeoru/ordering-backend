package com.server.ordering.api;

import com.server.ordering.domain.dto.ResultDto;
import com.server.ordering.service.RestaurantService;
import com.server.ordering.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WaitingApiController {

    private final WaitingService waitingService;

    @PutMapping("/api/waiting")
    public ResultDto<Boolean> addWaitingCnt(@RequestParam(name = "restaurant_id") Long restaurantId) {
        waitingService.addWaitingCnt(restaurantId);
        return new ResultDto<>(1, true);
    }
}

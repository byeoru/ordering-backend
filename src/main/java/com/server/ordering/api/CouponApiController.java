package com.server.ordering.api;

import com.server.ordering.domain.dto.ResultDto;
import com.server.ordering.domain.dto.request.CouponDto;
import com.server.ordering.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponApiController {

    private final CouponService couponService;

    /**
     * 쿠폰 등록
     */
    @PostMapping("/api/coupon")
    public ResultDto<Boolean> registerCoupon(@RequestBody CouponDto dto) {
        couponService.saveCoupon(dto);
        return new ResultDto<>(1, true);
    }
}

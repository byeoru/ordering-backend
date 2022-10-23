package com.byeoru.ordering_server.api

import com.byeoru.ordering_server.domain.dto.ResultDto
import com.byeoru.ordering_server.domain.dto.request.CouponDto
import com.byeoru.ordering_server.service.CouponService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class CouponApiController(val couponService: CouponService) {

    /**
     * 쿠폰 등록
     */
    @PostMapping("/api/coupon")
    fun registerCoupon(@RequestBody dto: CouponDto): ResultDto<Boolean> {
        couponService.saveCoupon(dto)
        return ResultDto(1, true)
    }
}
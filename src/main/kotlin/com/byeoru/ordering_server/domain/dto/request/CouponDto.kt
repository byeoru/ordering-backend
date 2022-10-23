package com.byeoru.ordering_server.domain.dto.request

import com.byeoru.ordering_server.domain.MyCoupon

class CouponDto {

    var couponId: Long? = null
    val serialNumber: String
    val value: Int

    constructor(myCoupon: MyCoupon) {
        couponId = myCoupon.id
        serialNumber = myCoupon.serialNumber
        value = myCoupon.value
    }

    constructor(serialNumber: String, value: Int) {
        this.serialNumber = serialNumber
        this.value = value
    }
}
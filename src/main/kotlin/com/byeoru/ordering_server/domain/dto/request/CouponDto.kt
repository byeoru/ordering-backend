package com.byeoru.ordering_server.domain.dto.request

import com.byeoru.ordering_server.domain.MyCoupon

class CouponDto(myCoupon: MyCoupon) {

    var couponId: Long? = null
    val serialNumber: String
    val value: Int

    init {
        couponId = myCoupon.id
        serialNumber = myCoupon.serialNumber
        value = myCoupon.value
    }
}
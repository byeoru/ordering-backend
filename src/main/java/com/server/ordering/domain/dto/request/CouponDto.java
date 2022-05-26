package com.server.ordering.domain.dto.request;

import com.server.ordering.domain.MyCoupon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class CouponDto {

    private Long couponId;
    private String serialNumber;
    private int value;

    public CouponDto(MyCoupon myCoupon) {
        this.couponId = myCoupon.getId();
        this.serialNumber = myCoupon.getSerialNumber();
        this.value = myCoupon.getValue();
    }
}

package com.server.ordering.domain.dto.request;

import com.server.ordering.domain.Coupon;
import com.server.ordering.domain.MyCoupon;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@RequiredArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class CouponDto {

    private Long couponId;
    @NonNull
    private String serialNumber;
    @NonNull
    private int value;

    public CouponDto(MyCoupon myCoupon) {
        this.couponId = myCoupon.getId();
        this.serialNumber = myCoupon.getSerialNumber();
        this.value = myCoupon.getValue();
    }
}

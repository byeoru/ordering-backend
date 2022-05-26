package com.server.ordering.service;

import com.server.ordering.domain.Coupon;
import com.server.ordering.domain.MyCoupon;
import com.server.ordering.domain.dto.request.CouponDto;
import com.server.ordering.domain.member.Customer;
import com.server.ordering.repository.CouponRepository;
import com.server.ordering.repository.CustomerRepository;
import com.server.ordering.repository.MyCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CouponService {

    private final CouponRepository couponRepository;
    private final MyCouponRepository myCouponRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public void saveCoupon(CouponDto couponDto) {
        Coupon coupon = new Coupon(couponDto.getSerialNumber(), couponDto.getValue());
        couponRepository.save(coupon);
    }

    public Coupon getCoupon(String id) {
        Coupon coupon = couponRepository.find(id);
        if (coupon == null) {
            throw new NoResultException();
        }
        return coupon;
    }

    public Boolean haveThisCoupon(String serialNumber, Long customerId) {
        try {
            myCouponRepository.findOneWithCustomer(serialNumber, customerId);
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Transactional
    public void useMyCoupon(Long couponId) {
        myCouponRepository.remove(couponId);
    }

    @Transactional
    public void saveMyCoupon(Coupon coupon, Long customerId) {
        Customer customer = customerRepository.findOne(customerId);
        MyCoupon myCoupon = new MyCoupon(coupon.getSerialNumber(), coupon.getValue(), customer);
        myCouponRepository.save(myCoupon);
    }
}

package com.byeoru.ordering_server.service

import com.byeoru.ordering_server.domain.Coupon
import com.byeoru.ordering_server.domain.MyCoupon
import com.byeoru.ordering_server.domain.dto.request.CouponDto
import com.byeoru.ordering_server.repository.CouponRepository
import com.byeoru.ordering_server.repository.CustomerRepository
import com.byeoru.ordering_server.repository.MyCouponRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.NoResultException

@Service
@Transactional(readOnly = true)
class CouponService(private val couponRepository: CouponRepository,
                    private val myCouponRepository: MyCouponRepository,
                    private val customerRepository: CustomerRepository) {

    @Transactional
    fun saveCoupon(couponDto: CouponDto) {
        val coupon = Coupon(couponDto.serialNumber, couponDto.value)
        couponRepository.save(coupon)
    }

    fun getCoupon(id: String): Coupon {
        return couponRepository.find(id)
    }

    fun haveThisCoupon(serialNumber: String?, customerId: Long?): Boolean {
        return try {
            myCouponRepository.findOneWithCustomer(serialNumber, customerId)
            true
        } catch (e: NoResultException) {
            false
        }
    }

    @Transactional
    fun useMyCoupon(couponId: Long) = myCouponRepository.remove(couponId)

    @Transactional
    fun saveMyCoupon(coupon: Coupon, customerId: Long) {
        val customer = customerRepository.findOne(customerId)
        val myCoupon = MyCoupon(coupon.serialNumber, coupon.value, customer)
        myCouponRepository.save(myCoupon)
    }

    companion object {
        private val log = LoggerFactory.getLogger(CouponService::class.java)
    }
}
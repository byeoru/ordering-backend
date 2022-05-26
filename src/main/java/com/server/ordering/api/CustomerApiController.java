package com.server.ordering.api;

import com.server.ordering.domain.*;
import com.server.ordering.domain.dto.*;
import com.server.ordering.domain.dto.request.*;
import com.server.ordering.domain.dto.response.BasketFood;
import com.server.ordering.domain.dto.response.CustomerSignInResultDto;
import com.server.ordering.domain.dto.response.MyWaitingInfoDto;
import com.server.ordering.domain.member.Customer;
import com.server.ordering.service.CouponService;
import com.server.ordering.service.CustomerService;
import com.server.ordering.service.VerificationService;
import com.server.ordering.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CustomerApiController {

    private final VerificationService verificationService;
    private final CustomerService customerService;
    private final CouponService couponService;
    private final WaitingService waitingService;

    /**
     *
     * 인증번호 받기
     */
    @PostMapping("/api/customer/verification/get")
    public ResultDto<Boolean> sendCode(@RequestBody PhoneNumberDto dto) {

        boolean bDuplicatedNumber = verificationService.isPhoneNumberDuplicated(MemberType.CUSTOMER, dto.getPhoneNumber());
        if (bDuplicatedNumber) {
            return new ResultDto<>(1, false);
        }

        verificationService.sendCode(dto.getPhoneNumber());
        return new ResultDto<>(1, true);
    }

    /**
     *
     * 인증번호 체크
     */
    @PostMapping("/api/customer/verification/check")
    public ResultDto<Boolean> checkCode(@RequestBody VerificationDto dto) {
        boolean isSuccess = verificationService.checkCode(dto.getTotalNum(), dto.getCode());
        return new ResultDto<>(1, isSuccess);
    }

    /**
     *
     * 고객 회원 가입
     */
    @PostMapping("/api/customer/signup")
    public ResultDto<Optional<Long>> singUp(@RequestBody CustomerSignUpDto dto) {
        PhoneNumber phoneNumber = new PhoneNumber(dto.getPhoneNumber(), MemberType.CUSTOMER);
        boolean bIdDuplicated = customerService.isIdDuplicated(dto.getSignInId());
        if (bIdDuplicated) {
            return new ResultDto<>(1, Optional.empty());
        } else {
            Customer customer = new Customer(dto.getNickname(), dto.getSignInId(), dto.getPassword(), phoneNumber);
            Optional<Long> optionalId = customerService.signUp(customer);
            return new ResultDto<>(1, optionalId);
        }
    }

    /**
     *
     * 고객 로그인
     */
    @PostMapping("/api/customer/signin")
    public ResultDto<CustomerSignInResultDto> signIn(@RequestBody SignInDto dto) {
        Optional<Customer> optionalCustomer = customerService.signIn(dto);
        return optionalCustomer.map(customer -> {
            int basketCount = customerService.getBasketCount(customer.getId());
            return new ResultDto<>(1, new CustomerSignInResultDto(customer.getId(), customer.getNickname(), basketCount));
        }).orElseGet(() -> new ResultDto<>(1, null));
    }

    /**
     *
     * 고객 휴대폰번호 변경
     */
    @PutMapping("/api/customer/{customerId}/phone_number")
    public ResultDto<Boolean> putPhoneNumber(@PathVariable Long customerId, @RequestBody PhoneNumberDto dto) {
        customerService.putPhoneNumber(customerId, dto.getPhoneNumber());
        return new ResultDto<>(1, true);
    }

    /**
     *
     * 고객 비밀번호 변경
     */
    @PutMapping("/api/customer/{customerId}/password")
    public ResultDto<Boolean> putPassword(@PathVariable Long customerId, @RequestBody PasswordChangeDto dto) {
        Boolean isChanged = customerService.putPassword(customerId, dto);
        return new ResultDto<>(1, isChanged);
    }

    /**
     *
     * 고객 계정 삭제
     */
    @DeleteMapping("/api/customer/{customerId}")
    public ResultDto<Boolean> delete(@PathVariable Long customerId) {
        customerService.deleteAccount(customerId);
        return new ResultDto<>(1, true);
    }

    /**
     * 고객 리뷰 등록
     */
    @PostMapping("/api/customer/review")
    public ResultDto<Boolean> registerReview(@RequestParam(name = "restaurant_id") Long restaurantId,
                                             @RequestParam(name = "order_id") Long orderId,
                                             @RequestBody ReviewDto dto) {
        Boolean isRegistered = customerService.registerReview(restaurantId, orderId, dto);
        return new ResultDto<>(1, isRegistered);
    }

    /**
     * 고객 리뷰 수정
     */
    @PutMapping("/api/customer/review/{reviewId}")
    public ResultDto<Boolean> putReview(@PathVariable Long reviewId, @RequestBody ReviewDto dto) {
        customerService.putReview(reviewId, dto);
        return new ResultDto<>(1, true);
    }

    /**
     * 고객 리뷰 삭제
     */
    @DeleteMapping("/api/customer/review/{reviewId}")
    public ResultDto<Boolean> putReview(@PathVariable Long reviewId) {
        customerService.removeReview(reviewId);
        return new ResultDto<>(1, true);
    }

    /**
     * 고객 쿠폰 등록
     */
    @PostMapping("/api/customer/{customerId}/coupon")
    public ResultDto<Boolean> getCoupon(@PathVariable Long customerId, @RequestBody CouponSerialNumberDto dto) {
        Coupon coupon = couponService.getCoupon(dto.getSerialNumber());
        Boolean haveCoupon = couponService.haveThisCoupon(dto.getSerialNumber(), customerId);
        if (haveCoupon) {
            return new ResultDto<>(1, false);
        }
        couponService.saveMyCoupon(coupon, customerId);
        return new ResultDto<>(1, true);
    }

    /**
     * 고객 쿠폰 사용
     */
    @DeleteMapping("/api/customer/coupon/{couponId}")
    public ResultDto<Boolean> useCoupon(@PathVariable Long couponId) {
        couponService.useMyCoupon(couponId);
        return new ResultDto<>(1, true);
    }

    /**
     * 내 웨이팅 조회
     */
    @PostMapping("/api/customer/{customerId}/waiting")
    public ResultDto<MyWaitingInfoDto> getMyWaitingInfo(@PathVariable Long customerId) {

        Waiting waiting = waitingService.findOneWithRestaurantByCustomer(customerId);

        // 접수한 웨이팅이 없다면 Null 반환
        if (waiting == null) {
            return new ResultDto<>(1, null);
        }

        // 앞에 남은 사람 수
        long numberInFrontOfMe = waitingService.
                getNumberInFrontOfMe(waiting.getMyWaitingNumber(), waiting.getRestaurant().getId());

        MyWaitingInfoDto myWaitingDto = new MyWaitingInfoDto(waiting, numberInFrontOfMe);
        return new ResultDto<>(1, myWaitingDto);
    }

    /**
     * 장바구니 리스트 조회
     */
    @PostMapping("/api/customer/{customerId}/baskets")
    public ResultDto<List<BasketFood>> getBasketList(@PathVariable Long customerId) {
        List<Basket> baskets = customerService.findBasketWithFood(customerId);
        List<BasketFood> basketResponseDtos = baskets.stream().map(BasketFood::new).collect(Collectors.toList());
        return new ResultDto<>(basketResponseDtos.size(), basketResponseDtos);
    }

    /**
     * 내 쿠폰 리스트 조회
     */
    @PostMapping("/api/customer/{customerId}/my_coupons")
    public ResultDto<List<CouponDto>> getMyCouponList(@PathVariable Long customerId) {
        List<MyCoupon> myCoupons = customerService.findMyCoupon(customerId);
        List<CouponDto> couponDtos = myCoupons.stream().map(CouponDto::new).collect(Collectors.toList());
        return new ResultDto<>(couponDtos.size(), couponDtos);
    }
}

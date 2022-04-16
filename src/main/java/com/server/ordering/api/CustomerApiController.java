package com.server.ordering.api;

import com.server.ordering.domain.MemberType;
import com.server.ordering.domain.PhoneNumber;
import com.server.ordering.domain.dto.*;
import com.server.ordering.domain.dto.request.*;
import com.server.ordering.domain.dto.response.CustomerSignInResultDto;
import com.server.ordering.domain.member.Customer;
import com.server.ordering.service.CustomerService;
import com.server.ordering.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CustomerApiController {

    private final VerificationService verificationService;
    private final CustomerService customerService;

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
        Optional<Customer> optionalCustomer = customerService.signIn(dto.getSignInId(), dto.getPassword());
        return optionalCustomer.map(customer -> new ResultDto<>(1,
                        new CustomerSignInResultDto(customer.getId(), customer.getNickname())))
                .orElseGet(() -> new ResultDto<>(1, null));
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
        Boolean isRegistered = customerService.registerReview(restaurantId, orderId, dto.getReview());
        return new ResultDto<>(1, isRegistered);
    }

    /**
     * 고객 리뷰 수정
     */
    @PutMapping("/api/customer/review/{reviewId}")
    public ResultDto<Boolean> putReview(@PathVariable Long reviewId, @RequestBody ReviewDto dto) {
        customerService.putReview(reviewId, dto.getReview());
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
}

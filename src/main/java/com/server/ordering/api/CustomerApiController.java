package com.server.ordering.api;

import com.server.ordering.domain.MemberType;
import com.server.ordering.domain.PhoneNumber;
import com.server.ordering.domain.dto.*;
import com.server.ordering.domain.dto.request.*;
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

    @PostMapping("/api/customer/verification/get")
    public ResultDto<Boolean> sendCode(@RequestBody PhoneNumberDto dto) {

        boolean bDuplicatedNumber = verificationService.isPhoneNumberDuplicated(MemberType.CUSTOMER, dto.getPhoneNumber());
        if (bDuplicatedNumber) {
            return new ResultDto<>(1, false);
        }

        verificationService.sendCode(dto.getPhoneNumber());
        return new ResultDto<>(1, true);
    }

    @PostMapping("/api/customer/verification/check")
    public ResultDto<Boolean> checkCode(@RequestBody VerificationDto dto) {
        boolean isSuccess = verificationService.checkCode(dto.getTotalNum(), dto.getCode());
        return new ResultDto<>(1, isSuccess);
    }

    @PostMapping("/api/customer/signup")
    public ResultDto<Optional<Long>> singUp(@RequestBody CustomerSignUpDto dto) {
        PhoneNumber phoneNumber = new PhoneNumber(dto.getPhoneNumber(), MemberType.CUSTOMER);
        boolean bIdDuplicated = customerService.isIdDuplicated(dto.getSignInId());
        if (bIdDuplicated) {
            return new ResultDto<>(1, Optional.empty());
        } else {
            verificationService.registerPhoneNumber(phoneNumber);
            Customer customer = new Customer(dto.getNickname(), dto.getSignInId(), dto.getPassword(), phoneNumber);
            Optional<Long> optionalId = customerService.signUp(customer);
            return new ResultDto<>(1, optionalId);
        }
    }

    @PostMapping("/api/customer/signin")
    public ResultDto<Long> signIn(@RequestBody SignInDto dto) {
        Optional<Customer> optionalCustomer = customerService.signIn(dto.getSignInId(), dto.getPassword());
        return optionalCustomer.map(customer -> new ResultDto<>(1, customer.getId()))
                .orElseGet(() -> new ResultDto<>(1, null));
    }

    @DeleteMapping("/api/customer/{customerId}")
    public ResultDto<Boolean> delete(@PathVariable Long customerId) {
        customerService.deleteAccount(customerId);
        return new ResultDto<>(1, true);
    }
}

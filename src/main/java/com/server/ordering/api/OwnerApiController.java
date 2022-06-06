package com.server.ordering.api;

import com.server.ordering.domain.MemberType;
import com.server.ordering.domain.Restaurant;
import com.server.ordering.domain.dto.*;
import com.server.ordering.domain.dto.request.*;
import com.server.ordering.domain.dto.response.OwnerSignInResultDto;
import com.server.ordering.domain.member.Owner;
import com.server.ordering.service.OwnerService;
import com.server.ordering.service.RestaurantService;
import com.server.ordering.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OwnerApiController {

    private final VerificationService verificationService;
    private final OwnerService ownerService;
    private final RestaurantService restaurantService;


    /**
     * 인증번호 받기
     */
    @PostMapping("/api/owner/verification/get")
    public ResultDto<Boolean> sendCode(@RequestBody PhoneNumberDto dto) {

        boolean bDuplicatedNumber = verificationService.isPhoneNumberDuplicated(MemberType.OWNER, dto.getPhoneNumber());
        if (bDuplicatedNumber) {
            return new ResultDto<>(1, false);
        } else {
            verificationService.sendCode(dto.getPhoneNumber());
            return new ResultDto<>(1, true);
        }
    }

    /**
     * 인증번호 체크
     */
    @PostMapping("/api/owner/verification/check")
    public ResultDto<Boolean> checkCode(@RequestBody VerificationDto dto) {
        boolean isSuccess = verificationService.checkCode(dto.getTotalNum(), dto.getCode());
        return new ResultDto<>(1, isSuccess);
    }

    /**
     * 점주 회원 가입
     */
    @PostMapping("/api/owner/signup")
    public ResultDto<Long> singUp(@RequestBody OwnerSignUpDto dto) {
        boolean bIdDuplicated = ownerService.isIdDuplicated(dto.getSignInId());
        if (bIdDuplicated) {
            return new ResultDto<>(1, null);
        } else {
            Long ownerId = ownerService.signUp(dto);
            return new ResultDto<>(1, ownerId);
        }
    }

    /**
     * 점주 로그인
     */
    @PostMapping("/api/owner/signin")
    public ResultDto<OwnerSignInResultDto> signIn(@RequestBody SignInDto dto) {
        Owner owner = ownerService.signIn(dto);

        if (owner != null) {
            Restaurant restaurant = owner.getRestaurant();

            if (restaurant != null) {
                return new ResultDto<>(1, new OwnerSignInResultDto(owner.getId(), owner.getRestaurant()));
            }
            return new ResultDto<>(1, new OwnerSignInResultDto(owner.getId()));
        } else {
            return new ResultDto<>(1, null);
        }
    }

    /**
     * 점주 로그아웃
     */
    @PostMapping("/api/owner/{ownerId}/sign_out")
    public ResultDto<Boolean> signOut(@PathVariable Long ownerId) {
        ownerService.signOut(ownerId);
        return new ResultDto<>(1, true);
    }


    /**
     * 점주 휴대폰번호 변경
     */
    @PutMapping("/api/owner/{ownerId}/phone_number")
    public ResultDto<Boolean> putPhoneNumber(@PathVariable Long ownerId, @RequestBody PhoneNumberDto dto) {
        ownerService.putPhoneNumber(ownerId, dto.getPhoneNumber());
        return new ResultDto<>(1, true);
    }

    /**
     * 점주 비밀번호 변경
     */
    @PutMapping("/api/owner/{ownerId}/password")
    public ResultDto<Boolean> putPassword(@PathVariable Long ownerId, @RequestBody PasswordChangeDto dto) {
        Boolean isChanged = ownerService.putPassword(ownerId, dto);
        return new ResultDto<>(1, isChanged);
    }

    /**
     * 점주 계정 삭제
     */
    @DeleteMapping("/api/owner/{ownerId}")
    public ResultDto<Boolean> delete(@PathVariable Long ownerId) {
        ownerService.deleteAccount(ownerId);
        return new ResultDto<>(1, true);
    }

    /**
     * 점주 매장 등록
     */
    @PostMapping("/api/owner/{ownerId}/restaurant")
    public ResultDto<Long> registerRestaurant(
            @PathVariable Long ownerId,
            @RequestBody RestaurantDataWithLocationDto dto) {

        Long restaurantId = restaurantService.registerRestaurant(ownerId, dto);
        return new ResultDto<>(1, restaurantId);
    }
}

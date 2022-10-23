package com.byeoru.ordering_server.api

import com.byeoru.ordering_server.domain.MemberType
import com.byeoru.ordering_server.domain.dto.ResultDto
import com.byeoru.ordering_server.domain.dto.request.*
import com.byeoru.ordering_server.domain.dto.response.OwnerSignInResultDto
import com.byeoru.ordering_server.service.OwnerService
import com.byeoru.ordering_server.service.RestaurantService
import com.byeoru.ordering_server.service.VerificationService
import org.springframework.web.bind.annotation.*

@RestController
class OwnerApiController(val verificationService: VerificationService,
                         val ownerService: OwnerService,
                         val restaurantService: RestaurantService) {

    /**
     * 인증번호 받기
     */
    @PostMapping("/api/owner/verification/get")
    fun sendCode(@RequestBody dto: PhoneNumberDto): ResultDto<Boolean> {
        val bDuplicatedNumber = verificationService.isPhoneNumberDuplicated(MemberType.OWNER, dto.phoneNumber)
        return if (bDuplicatedNumber) {
            ResultDto(1, false)
        } else {
            verificationService.sendCode(dto.phoneNumber)
            ResultDto(1, true)
        }
    }

    /**
     * 인증번호 체크
     */
    @PostMapping("/api/owner/verification/check")
    fun checkCode(@RequestBody dto: VerificationDto): ResultDto<Boolean> {
        val isSuccess = verificationService.checkCode(dto.totalNum, dto.code)
        return ResultDto(1, isSuccess)
    }

    /**
     * 점주 회원 가입
     */
    @PostMapping("/api/owner/signup")
    fun singUp(@RequestBody dto: OwnerSignUpDto): ResultDto<Long> {
        val bIdDuplicated = ownerService.isIdDuplicated(dto.signInId)
        return if (bIdDuplicated) {
            ResultDto(1, null)
        } else {
            val ownerId = ownerService.signUp(dto)
            ResultDto(1, ownerId)
        }
    }

    /**
     * 점주 로그인
     */
    @PostMapping("/api/owner/signin")
    fun signIn(@RequestBody dto: SignInDto): ResultDto<OwnerSignInResultDto> {
        val owner = ownerService.signIn(dto)
        return if (owner != null) {
            val restaurant = owner.restaurant
            if (restaurant != null) ResultDto(1, OwnerSignInResultDto(owner.id, owner.restaurant!!))
            else ResultDto(1, OwnerSignInResultDto(owner.id))
        } else {
            ResultDto(1, null)
        }
    }

    /**
     * 점주 로그아웃
     */
    @PostMapping("/api/owner/{ownerId}/sign_out")
    fun signOut(@PathVariable ownerId: Long): ResultDto<Boolean> {
        ownerService.signOut(ownerId)
        return ResultDto(1, true)
    }

    /**
     * 점주 휴대폰번호 변경
     */
    @PutMapping("/api/owner/{ownerId}/phone_number")
    fun putPhoneNumber(@PathVariable ownerId: Long,
                       @RequestBody dto: PhoneNumberDto): ResultDto<Boolean> {
        ownerService.putPhoneNumber(ownerId, dto.phoneNumber)
        return ResultDto(1, true)
    }

    /**
     * 점주 비밀번호 변경
     */
    @PutMapping("/api/owner/{ownerId}/password")
    fun putPassword(@PathVariable ownerId: Long,
                    @RequestBody dto: PasswordChangeDto): ResultDto<Boolean> {
        val isChanged = ownerService.putPassword(ownerId, dto)
        return ResultDto(1, isChanged)
    }

    /**
     * 점주 계정 삭제
     */
    @DeleteMapping("/api/owner/{ownerId}")
    fun delete(@PathVariable ownerId: Long): ResultDto<Boolean> {
        ownerService.deleteAccount(ownerId)
        return ResultDto(1, true)
    }

    /**
     * 점주 매장 등록
     */
    @PostMapping("/api/owner/{ownerId}/restaurant")
    fun registerRestaurant(@PathVariable ownerId: Long,
                           @RequestBody dto: RestaurantDataWithLocationDto): ResultDto<Long> {
        val restaurantId = restaurantService.registerRestaurant(ownerId, dto)
        return ResultDto(1, restaurantId)
    }
}
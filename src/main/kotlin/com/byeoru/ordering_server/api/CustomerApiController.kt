package com.byeoru.ordering_server.api

import com.byeoru.ordering_server.domain.*
import com.byeoru.ordering_server.domain.dto.ResultDto
import com.byeoru.ordering_server.domain.dto.request.*
import com.byeoru.ordering_server.domain.dto.response.*
import com.byeoru.ordering_server.service.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
class CustomerApiController(val verificationService: VerificationService,
                            val customerService: CustomerService,
                            val couponService: CouponService,
                            val waitingService: WaitingService,
                            val orderService: OrderService,
                            val restaurantService: RestaurantService) {

    /**
     * 인증번호 받기
     */
    @PostMapping("/api/customer/verification/get")
    fun sendCode(@RequestBody dto: PhoneNumberDto): ResultDto<Boolean> {
        val bDuplicatedNumber: Boolean = verificationService.isPhoneNumberDuplicated(MemberType.CUSTOMER, dto.phoneNumber)
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
    @PostMapping("/api/customer/verification/check")
    fun checkCode(@RequestBody dto: VerificationDto): ResultDto<Boolean> {
        val isSuccess: Boolean = verificationService.checkCode(dto.totalNum, dto.code)
        return ResultDto(1, isSuccess)
    }

    /**
     * 고객 회원 가입
     */
    @PostMapping("/api/customer/signup")
    fun singUp(@RequestBody dto: CustomerSignUpDto): ResultDto<Long> {
        val bIdDuplicated: Boolean = customerService.isIdDuplicated(dto.signInId)
        return if (bIdDuplicated) {
            ResultDto(1, null)
        } else {
            val customerId: Long? = customerService.signUp(dto)
            ResultDto(1, customerId)
        }
    }

    /**
     * 고객 로그인
     */
    @PostMapping("/api/customer/signin")
    fun signIn(@RequestBody dto: SignInDto): ResultDto<CustomerSignInResultDto> {
        val customer = customerService.signIn(dto) ?: return ResultDto(1, null)
        val basketCount = customerService.getBasketCount(customer.id!!)
        return ResultDto(1, CustomerSignInResultDto(customer.id!!, customer.nickname, basketCount))
    }

    /**
     * 고객 로그아웃
     */
    @PostMapping("/api/customer/{customerId}/sign_out")
    fun signOut(@PathVariable customerId: Long): ResultDto<Boolean> {
        customerService.signOut(customerId)
        return ResultDto(1, true)
    }

    /**
     * 고객 휴대폰번호 변경
     */
    @PutMapping("/api/customer/{customerId}/phone_number")
    fun putPhoneNumber(@PathVariable customerId: Long,
                       @RequestBody dto: PhoneNumberDto): ResultDto<Boolean> {
        customerService.putPhoneNumber(customerId, dto.phoneNumber)
        return ResultDto(1, true)
    }

    /**
     * 고객 비밀번호 변경
     */
    @PutMapping("/api/customer/{customerId}/password")
    fun putPassword(@PathVariable customerId: Long,
                    @RequestBody dto: PasswordChangeDto): ResultDto<Boolean> {
        val isChanged = customerService.putPassword(customerId, dto)
        return ResultDto(1, isChanged)
    }

    /**
     * 고객 계정 삭제
     */
    @DeleteMapping("/api/customer/{customerId}")
    fun delete(@PathVariable customerId: Long): ResultDto<Boolean> {
        customerService.deleteAccount(customerId)
        return ResultDto(1, true)
    }

    /**
     * 고객 리뷰 등록
     */
    @PostMapping("/api/customer/review")
    fun registerReview(@RequestParam(name = "restaurant_id") restaurantId: Long,
                       @RequestParam(name = "order_id") orderId: Long,
                       @RequestPart(required = false) image: MultipartFile?,
                       @RequestPart dto: ReviewDto): ResultDto<Boolean> {
        val isRegistered = customerService.registerReview(restaurantId, orderId, dto, image)
        return ResultDto(1, isRegistered)
    }

    /**
     * 고객 리뷰 삭제
     */
    @DeleteMapping("/api/customer/review/{reviewId}")
    fun putReview(@PathVariable reviewId: Long): ResultDto<Boolean> {
        customerService.removeReview(reviewId)
        return ResultDto(1, true)
    }

    /**
     * 고객 쿠폰 등록
     */
    @PostMapping("/api/customer/{customerId}/coupon")
    fun getCoupon(@PathVariable customerId: Long,
                  @RequestBody dto: CouponSerialNumberDto): ResultDto<Boolean> {
        val coupon = couponService.getCoupon(dto.serialNumber)
        val haveCoupon = couponService.haveThisCoupon(dto.serialNumber, customerId)
        return if (haveCoupon) {
            ResultDto(1, false)
        } else {
            couponService.saveMyCoupon(coupon, customerId)
            ResultDto(1, true)
        }
    }

    /**
     * 고객 쿠폰 사용
     */
    @DeleteMapping("/api/customer/coupon/{couponId}")
    fun useCoupon(@PathVariable couponId: Long): ResultDto<Boolean> {
        couponService.useMyCoupon(couponId)
        return ResultDto(1, true)
    }

    /**
     * 내 웨이팅 조회
     */
    @PostMapping("/api/customer/{customerId}/waiting")
    fun getMyWaitingInfo(@PathVariable customerId: Long): ResultDto<MyWaitingInfoDto> {
        // 접수한 웨이팅이 없다면 Null 반환
        val waiting = waitingService.findOneWithRestaurantByCustomer(customerId) ?: return ResultDto(1, null)
        // 앞에 남은 사람 수
        val numberInFrontOfMe = waitingService.getNumberInFrontOfMe(waiting.myWaitingNumber, waiting.restaurant.id!!)
        val myWaitingDto = MyWaitingInfoDto(waiting, numberInFrontOfMe)
        return ResultDto(1, myWaitingDto)
    }

    /**
     * 장바구니 리스트 조회
     */
    @PostMapping("/api/customer/{customerId}/baskets")
    fun getBasketList(@PathVariable customerId: Long): ResultDto<BasketListResultDto> {
        val customer = customerService.findCustomerWithBasketWithFood(customerId)
        var restaurantName: String? = null

        customer.basketKey?.let {
            restaurantName = restaurantService.findRestaurant(it).restaurantName
        }

        val basketResponseDtos = customer.baskets.map { BasketFood(it) }
        val basketListResultDto = BasketListResultDto(restaurantName, basketResponseDtos)
        return ResultDto(1, basketListResultDto)
    }

    /**
     * 내 쿠폰 리스트 조회
     */
    @PostMapping("/api/customer/{customerId}/my_coupons")
    fun getMyCouponList(@PathVariable customerId: Long): ResultDto<List<CouponDto>> {
        val myCoupons = customerService.findMyCoupon(customerId)
        val couponDtos = myCoupons.map { CouponDto(it) }
        return ResultDto(couponDtos.size, couponDtos)
    }

    /**
     * 매장 북마크(찜)
     */
    @PostMapping("/api/customer/{customerId}/bookmark")
    fun restaurantBookmark(@PathVariable customerId: Long,
                           @RequestParam(name = "restaurant_id") restaurantId: Long): ResultDto<Long> {
        // 중복 체크
        val bExist: Boolean = customerService.isExistedBookmark(customerId, restaurantId)
        // 이미 북마크 되어 있음
        return if (bExist) {
            ResultDto(1, null)
        } else {
            val bookmarkId = customerService.addBookmark(customerId, restaurantId)
            ResultDto(1, bookmarkId)
        }
    }

    /**
     * 매장 북마크(찜) 해제
     */
    @DeleteMapping("/api/customer/bookmark/{bookmarkId}")
    fun deleteBookmark(@PathVariable bookmarkId: Long): ResultDto<Boolean> {
        customerService.removeBookmark(bookmarkId)
        return ResultDto(1, true)
    }

    /**
     * 내 북마크 불러오기
     */
    @GetMapping("/api/customer/{customerId}/bookmarks")
    fun getBookmarkList(@PathVariable customerId: Long): ResultDto<List<BookmarkPreviewDto>> {
        val bookmarks = customerService.findAllBookmarkWithRestWithRepresentative(customerId)
        val previewDtos = bookmarks.map { BookmarkPreviewDto(it) }
        return ResultDto(previewDtos.size, previewDtos)
    }

    /**
     * 내 주문 내역(진행 중) 리스트 불러오기
     */
    @GetMapping("/api/customer/{customerId}/orders/ongoing")
    fun getAllMyOngoingOrders(@PathVariable customerId: Long): ResultDto<List<OrderPreviewWithRestSimpleDto>> {
        val ongoingOrders = customerService.findAllMyOngoingOrders(customerId)
        val historyDtos = ongoingOrders.map { OrderPreviewWithRestSimpleDto(it) }
        return ResultDto(historyDtos.size, historyDtos)
    }

    /**
     * 내 주문 내역(완료) 리스트 조회
     */
    @GetMapping("/api/customer/{customerId}/orders/finished")
    fun getAllMyFinishedOrders(@PathVariable customerId: Long): ResultDto<List<OrderPreviewWithRestSimpleDto>> {
        val finishedOrders = customerService.findAllMyFinishedOrders(customerId)
        val historyDtos = finishedOrders.map { OrderPreviewWithRestSimpleDto(it) }
        return ResultDto(historyDtos.size, historyDtos)
    }

    /**
     * 내 주문 상세정보
     */
    @GetMapping("/api/customer/order/{orderId}/detail")
    fun getOrderDetail(@PathVariable orderId: Long): ResultDto<OrderDetailDto> {
        val order = orderService.findOrderWithRestWithOrderFoodsWithFood(orderId)
        val orderDetailDto = OrderDetailDto(order)
        return ResultDto(1, orderDetailDto)
    }

    /**
     * 최근 주문 매장 리스트 조회
     */
    @GetMapping("/api/customer/{customerId}/orders/recent")
    fun getRecentOrdersRestaurant(@PathVariable customerId: Long): ResultDto<List<RecentOrderRestaurantDto>> {
        val restaurantDtos = customerService.findRecentOrdersWithRestaurant(customerId, 10)
        return ResultDto(restaurantDtos.size, restaurantDtos)
    }
}
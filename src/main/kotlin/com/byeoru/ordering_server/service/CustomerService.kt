package com.byeoru.ordering_server.service

import com.byeoru.ordering_server.S3Service
import com.byeoru.ordering_server.domain.*
import com.byeoru.ordering_server.domain.dto.request.CustomerSignUpDto
import com.byeoru.ordering_server.domain.dto.request.PasswordChangeDto
import com.byeoru.ordering_server.domain.dto.request.ReviewDto
import com.byeoru.ordering_server.domain.dto.request.SignInDto
import com.byeoru.ordering_server.domain.member.Customer
import com.byeoru.ordering_server.repository.*
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import javax.persistence.NoResultException

@Service
@Transactional(readOnly = true)
class CustomerService(private val customerRepository: CustomerRepository,
                      private val reviewRepository: ReviewRepository,
                      private val restaurantRepository: RestaurantRepository,
                      private val orderRepository: OrderRepository,
                      private val basketRepository: BasketRepository,
                      private val myCouponRepository: MyCouponRepository,
                      private val bookmarkRepository: BookmarkRepository,
                      private val passwordEncoder: PasswordEncoder,
                      private val s3Service: S3Service) : MemberService<Customer> {

    fun findCustomerWithPhoneNumberWithWaiting(customerId: Long) =
        customerRepository.findOneWithPhoneNumberWithWaiting(customerId)

    /**
     * 고객 회원가입
     */
    @Transactional
    override fun signUp(signUpDto: Any): Long? {
        val customerSignUpDto = signUpDto as CustomerSignUpDto
        val phoneNumber = PhoneNumber(customerSignUpDto.phoneNumber, MemberType.CUSTOMER)

        // spring security password 암호화
        val encodedPassword = passwordEncoder.encode(customerSignUpDto.password)
        val customer = Customer(
            customerSignUpDto.nickname, customerSignUpDto.signInId,
            encodedPassword, phoneNumber
        )
        customerRepository.save(customer)
        return customer.id
    }

    /**
     * 고객 로그인
     */
    @Transactional
    override fun signIn(signInDto: SignInDto): Customer? {
        return try {
            val customer = customerRepository.findById(signInDto.signInId)

            // 사용자가 입력한 raw password, 해시화된 password 비교
            val bMatch = passwordEncoder.matches(signInDto.password, customer.password)
            if (!bMatch) {
                return null
            }

            // client 에서 받아온 토큰을 저장/업데이트
            customer.putFirebaseToken(signInDto.firebaseToken)
            customer
        } catch (e: NoResultException) {
            null
        }
    }

    @Transactional
    override fun signOut(id: Long) {
        val customer = customerRepository.findOne(id)
        customer.clearFirebaseToken()
    }

    override fun isIdDuplicated(signInId: String): Boolean {
        return try {
            customerRepository.findById(signInId)
            true
        } catch (e: NoResultException) {
            false
        }
    }

    /**
     * 고객 휴대폰번호 변경
     */
    @Transactional
    fun putPhoneNumber(customerId: Long, phoneNumber: String) {
        val customer = customerRepository.findOneWithPhoneNumber(customerId)
        customer.phoneNumber.putPhoneNumber(phoneNumber)
    }

    /**
     * 고객 비밀번호 변경
     */
    @Transactional
    fun putPassword(customerId: Long, dto: PasswordChangeDto): Boolean {
        val customer = customerRepository.findOne(customerId)
        if (passwordEncoder.matches(dto.currentPassword, customer.password)) {
            val encodedNewPassword = passwordEncoder.encode(dto.newPassword)
            customer.putPassword(encodedNewPassword)
            return true
        }
        return false
    }

    /**
     * 고객 회원탈퇴
     */
    @Transactional
    override fun deleteAccount(id: Long) {
        orderRepository.putCustomerIdToNull(id)
        customerRepository.remove(id)
    }

    /**
     * 고객 리뷰 등록
     */
    @Transactional
    fun registerReview(restaurantId: Long, orderId: Long, dto: ReviewDto, image: MultipartFile?): Boolean {
        val order = orderRepository.findOneWithReview(orderId)
        if (!order.isAbleRegisterReview) {
            return false
        }
        val review = Review(dto.review, dto.rating)
        if (image != null) {
            val newImageKey = String.format("%d%s%d", orderId, "review-image", System.currentTimeMillis())
            val imageUrl: String = s3Service.upload(image, newImageKey)
            review.registerImageUrl(imageUrl)
        }
        val restaurant = restaurantRepository.findOne(restaurantId)
        review.registerOrderAndRestaurant(order, restaurant)
        reviewRepository.save(review)
        return true
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    fun removeReview(reviewId: Long) = reviewRepository.remove(reviewId)

    /**
     * 매장 북마크(찜) 등록
     */
    @Transactional
    fun addBookmark(customerId: Long, restaurantId: Long): Long? {
        val customer = customerRepository.findOne(customerId)
        val restaurant = restaurantRepository.findOne(restaurantId)
        val bookmark = Bookmark(restaurant, customer)
        bookmarkRepository.save(bookmark)
        return bookmark.id
    }

    /**
     * 매장 북마크(찜) 삭제
     */
    @Transactional
    fun removeBookmark(bookmarkId: Long) = bookmarkRepository.remove(bookmarkId)

    /**
     * 북마크 리스트 불러오기
     */
    fun findAllBookmarkWithRestWithRepresentative(customerId: Long) =
        bookmarkRepository.findAllWithRestWithRepresentativeByCustomerId(customerId)

    /**
     * 북마크 중복 체크
     */
    fun isExistedBookmark(customerId: Long, restaurantId: Long): Boolean {
        return try {
            bookmarkRepository.findOneByCustomerIdAndRestaurantId(customerId, restaurantId)
            true
        } catch (e: NoResultException) {
            false
        }
    }

    /**
     * 장바구니 리스트 반환
     */
    fun findCustomerWithBasketWithFood(customerId: Long) =
        customerRepository.findOneWithBasketWithFood(customerId)

    /**
     * 장바구니 음식 개수 반환
     */
    fun getBasketCount(customerId: Long) = basketRepository.getBasketCount(customerId)

    /**
     * 내 쿠폰 조회
     */
    fun findMyCoupon(customerId: Long) = myCouponRepository.findAll(customerId)

    /**
     * 내 주문 내역 조회(진행 중)
     */
    fun findAllMyOngoingOrders(customerId: Long) =
        orderRepository.findAllOngoingWithRestWithOrderFoodsByCustomerId(customerId)

    /**
     * 내 주문 내역 조회(완료)
     */
    fun findAllMyFinishedOrders(customerId: Long) =
        orderRepository.findAllFinishedWithRestWithOrderFoodsByCustomerId(customerId)

    /**
     * 최근 주문 매장 조회(10개)
     */
    fun findRecentOrdersWithRestaurant(customerId: Long, requestNumber: Int) =
        orderRepository.findRecentWithRestaurant(customerId, requestNumber)

    companion object {
        private val log = LoggerFactory.getLogger(CustomerService::class.java)
    }
}
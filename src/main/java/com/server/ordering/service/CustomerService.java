package com.server.ordering.service;

import com.server.ordering.S3Service;
import com.server.ordering.domain.*;
import com.server.ordering.domain.dto.request.CustomerSignUpDto;
import com.server.ordering.domain.dto.request.PasswordChangeDto;
import com.server.ordering.domain.dto.request.ReviewDto;
import com.server.ordering.domain.dto.request.SignInDto;
import com.server.ordering.domain.dto.response.RecentOrderRestaurantDto;
import com.server.ordering.domain.member.Customer;
import com.server.ordering.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements MemberService<Customer> {

    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final BasketRepository basketRepository;
    private final MyCouponRepository myCouponRepository;
    private final BookmarkRepository bookmarkRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    public Customer findCustomerWithPhoneNumberWithWaiting(Long customerId) {
        return customerRepository.findOneWithPhoneNumberWithWaiting(customerId);
    }

    /**
     * 고객 회원가입
     * @return 가입한 고객 ID 반환
     */
    @Transactional
    @Override
    public Long signUp(Object signUpDto) {
        CustomerSignUpDto customerSignUpDto = (CustomerSignUpDto) signUpDto;
        PhoneNumber phoneNumber = new PhoneNumber(customerSignUpDto.getPhoneNumber(), MemberType.CUSTOMER);
        String encodedPassword = passwordEncoder.encode(customerSignUpDto.getPassword());
        Customer customer = new Customer(customerSignUpDto.getNickname(), customerSignUpDto.getSignInId(), encodedPassword, phoneNumber);
        customerRepository.save(customer);
        return customer.getId();
    }

    /**
     * 고객 로그인
     */
    @Transactional
    @Override
    public Customer signIn(SignInDto signInDto) {
        try {
            Customer customer = customerRepository.findById(signInDto.getSignInId());
            boolean bMatch = passwordEncoder.matches(signInDto.getPassword(), customer.getPassword());

            if (!bMatch) {
                return null;
            }

            customer.putFirebaseToken(signInDto.getFirebaseToken());
            return customer;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    @Override
    public void signOut(Long id) {
        Customer customer = customerRepository.findOne(id);
        customer.clearFirebaseToken();
    }

    /**
     * 고객 회원가입할 때 email 중복 검증
     */
    @Override
    public boolean isIdDuplicated(String signInId) {
        try {
            customerRepository.findById(signInId);
            return true;
        } catch (NoResultException e) {
            return false;
        }

    }

    /**
     * 고객 휴대폰번호 변경
     */
    @Transactional
    public void putPhoneNumber(Long customerId, String phoneNumber) {
        Customer customer = customerRepository.findOneWithPhoneNumber(customerId);
        customer.getPhoneNumber().putPhoneNumber(phoneNumber);
    }

    /**
     * 고객 비밀번호 변경
     */
    @Transactional
    public Boolean putPassword(Long customerId, PasswordChangeDto dto) {
        Customer customer = customerRepository.findOne(customerId);
        if (passwordEncoder.matches(customer.getPassword(), dto.getCurrentPassword())) {
            String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
            customer.putPassword(encodedNewPassword);
            return true;
        }
        return false;
    }

    /**
     * 고객 회원탈퇴
     */
    @Transactional
    @Override
    public void deleteAccount(Long id) {
        orderRepository.putCustomerIdToNull(id);
        customerRepository.remove(id);
    }

    /**
     * 고객 리뷰 등록
     */
    @Transactional
    public Boolean registerReview(Long restaurantId, Long orderId, ReviewDto dto, MultipartFile image) {

        Order order = orderRepository.findOneWithReview(orderId);

        if (!order.isAbleRegisterReview()) {
            return false;
        }

        Review review = new Review(dto.getReview(), dto.getRating());

        if (image != null) {
            String newImageKey = String.format("%d%s%d", orderId, "review-image", System.currentTimeMillis());
            String imageUrl = s3Service.upload(image, newImageKey);
            review.registerImageUrl(imageUrl);
        }

        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        review.registerOrderAndRestaurant(order, restaurant);
        reviewRepository.save(review);

        return true;
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public void removeReview(Long reviewId) {
        Review review = reviewRepository.findOne(reviewId);
        reviewRepository.remove(review);
    }

    /**
     * 매장 북마크(찜) 등록
     */
    @Transactional
    public Long addBookmark(Long customerId, Long restaurantId) {
        Customer customer = customerRepository.findOne(customerId);
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        Bookmark bookmark = new Bookmark(restaurant, customer);
        bookmarkRepository.save(bookmark);
        return bookmark.getId();
    }

    /**
     * 매장 북마크(찜) 삭제
     */
    @Transactional
    public void removeBookmark(Long bookmarkId) {
        bookmarkRepository.remove(bookmarkId);
    }

    /**
     * 북마크 리스트 불러오기
     */
    public List<Bookmark> findAllBookmarkWithRestWithRepresentative(Long customerId) {
        return bookmarkRepository.findAllWithRestWithRepresentativeByCustomerId(customerId);
    }

    /**
     * 북마크 중복 체크
     */
    public Boolean isExistedBookmark(Long customerId, Long restaurantId) {
        try {
            bookmarkRepository.findOneByCustomerIdAndRestaurantId(customerId, restaurantId);
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     * 장바구니 리스트 반환
     */
    public List<Basket> findBasketWithFood(Long customerId) {
        return basketRepository.findAllWithFoodByCustomerId(customerId);
    }

    /**
     * 장바구니 음식 개수 반환
     */
    public int getBasketCount(Long customerId) {
        return basketRepository.getBasketCount(customerId);
    }

    /**
     * 내 쿠폰 조회
     */
    public List<MyCoupon> findMyCoupon(Long customerId) {
        return myCouponRepository.findAll(customerId);
    }

    /**
     * 내 주문 내역 조회(진행 중)
     */
    public List<Order> findAllMyOngoingOrders(Long customerId) {
        return orderRepository.findAllOngoingWithRestWithOrderFoodsByCustomerId(customerId);
    }

    /**
     * 내 주문 내역 조회(완료)
     */
    public List<Order> findAllMyFinishedOrders(Long customerId) {
        return orderRepository.findAllFinishedWithRestWithOrderFoodsByCustomerId(customerId);
    }

    /**
     * 최근 주문 매장 조회(10개)
     */
    public List<RecentOrderRestaurantDto> findRecentOrdersWithRestaurant(Long customerId, int requestNumber) {
        return orderRepository.findRecentWithRestaurant(customerId, requestNumber);
    }
}

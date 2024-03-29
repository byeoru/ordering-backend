package com.server.ordering.service;

import com.server.ordering.FirebaseCloudMessageService;
import com.server.ordering.S3Service;
import com.server.ordering.domain.*;
import com.server.ordering.domain.dto.request.*;
import com.server.ordering.domain.dto.response.SalesResponseDto;
import com.server.ordering.domain.dto.response.RestaurantPreviewWithDistanceDto;
import com.server.ordering.domain.member.Owner;
import com.server.ordering.exception.FcmErrorException;
import com.server.ordering.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {

    private final S3Service s3Service;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final OwnerRepository ownerRepository;
    private final FoodRepository foodRepository;
    private final RepresentativeMenuRepository representativeMenuRepository;
    private final ReviewRepository reviewRepository;
    private final WaitingRepository waitingRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @Transactional
    public Long registerRestaurant(Long ownerId, RestaurantDataWithLocationDto dto) throws PersistenceException {

        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
        // client 에서 보낸 좌표값으로 Point Type 객체 생성
        Point point = factory.createPoint(new Coordinate(dto.getLongitude(), dto.getLatitude()));

        Restaurant restaurant = new Restaurant(dto.getRestaurantName(), dto.getOwnerName(), dto.getAddress(), point,
                dto.getTableCount(), dto.getFoodCategory(), dto.getRestaurantType(),
                dto.getOrderingWaitingTime(), dto.getAdmissionWaitingTime());

        restaurantRepository.save(restaurant);
        Owner owner = ownerRepository.findOne(ownerId);
        owner.registerRestaurant(restaurant);
        return restaurant.getId();
    }

    @Transactional
    public void putRestaurant(Long restaurantId, RestaurantDataWithLocationDto dto) throws PersistenceException {
        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = factory.createPoint(new Coordinate(dto.getLongitude(), dto.getLatitude()));
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        restaurant.putRestaurant(dto.getRestaurantName(), dto.getOwnerName(), dto.getAddress(), point,
                dto.getTableCount(), dto.getFoodCategory(), dto.getRestaurantType());
    }

    public List<SalesResponseDto> getDailySalesOfRestaurant(Long restaurantId, String yearAndMonth) {
        return orderRepository.getDailySales(restaurantId, yearAndMonth);
    }

    public List<SalesResponseDto> getMonthlySalesOfRestaurant(Long restaurantId, String year) {
        return orderRepository.getMonthlySales(restaurantId, year);
    }

    @Transactional
    public void putRestaurantProfileImage(Long restaurantId, MultipartFile image) {
        String imageKey = restaurantId + "restaurant-profile" + System.currentTimeMillis();
        String imageUrl = s3Service.upload(image, imageKey);
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        restaurant.putProfileImageUrl(imageUrl);
    }

    @Transactional
    public void putRestaurantBackgroundImage(Long restaurantId, MultipartFile image) {
        String imageKey = restaurantId + "restaurant-background" + System.currentTimeMillis();
        String imageUrl = s3Service.upload(image, imageKey);
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        restaurant.putBackgroundImageUrl(imageUrl);
    }

    public Boolean isExistRepresentativeMenu(Long foodId) {
        try {
            representativeMenuRepository.findOneByFoodId(foodId);
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Transactional
    public void addRepresentativeMenu(Long restaurantId, Long foodId) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        Food food = foodRepository.findOne(foodId);
        RepresentativeMenu representativeMenu = new RepresentativeMenu(restaurant, food, food.getFoodName());
        restaurant.addRepresentativeMenu(representativeMenu);
        representativeMenuRepository.save(representativeMenu);
    }

    public List<RepresentativeMenu> findAllRepresentative(Long restaurantId) {
        return representativeMenuRepository.findAllByRestaurantId(restaurantId);
    }

    @Transactional
    public void removeRepresentativeMenu(Long representativeMenuId) {
        representativeMenuRepository.remove(representativeMenuId);
    }

    @Transactional
    public List<RestaurantPreviewWithDistanceDto> getAllForPreview(RestaurantPreviewListReqDto dto) {
        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);

        // 사용자의 위도, 경도 값으로 2차원 좌표 생성
        Point point = factory.createPoint(new Coordinate(dto.getLatitude(), dto.getLongitude()));
        return restaurantRepository.findAllWithRepresentativeMenu(point, dto.getFoodCategory());
    }

    @Transactional
    public void putOrderWaitingTime(Long restaurantId, WaitingTimeDto waitingTimeDto) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        restaurant.putOrderWaitingTime(waitingTimeDto.getMinutes());
    }

    @Transactional
    public void putAdmissionWaitingTime(Long restaurantId, WaitingTimeDto waitingTimeDto) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        restaurant.putAdmissionWaitingTime(waitingTimeDto.getMinutes());
    }

    public Restaurant findRestaurant(Long restaurantId) {
        return restaurantRepository.findOne(restaurantId);
    }

    @Transactional
    public void putRestaurantNotice(Long restaurantId, MessageDto messageDto) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        restaurant.putNotice(messageDto.getMessage());
    }

    public List<Review> findReviewWithOrderWithCustomer(Long restaurantId) {
        return reviewRepository.findAllWithOrderWithCustomerByRestaurantId(restaurantId);
    }

    public void callWaiting(Long waitingId) {
        Waiting waiting = waitingRepository.findOneWithCustomerAndRestaurant(waitingId);
        String token = waiting.getCustomer().getFirebaseToken();

        String title = String.format("(웨이팅 호출) %s", waiting.getRestaurant().getRestaurantName());
        String body = String.format("대기번호 %d번님, 매장 입장해주세요!", waiting.getMyWaitingNumber());

        try {
            // 고객에게 웨이팅 호출 FCM 발신
            firebaseCloudMessageService.sendMessageTo(token, title, body, OrderType.WAITING);
        } catch (IOException e) {
            throw new FcmErrorException();
        }
    }
}
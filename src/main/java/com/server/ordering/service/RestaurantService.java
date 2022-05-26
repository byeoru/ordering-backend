package com.server.ordering.service;

import com.server.ordering.S3Service;
import com.server.ordering.domain.*;
import com.server.ordering.domain.dto.request.RestaurantInfoDto;
import com.server.ordering.domain.dto.request.RestaurantPreviewListReqDto;
import com.server.ordering.domain.dto.request.WaitingTimeDto;
import com.server.ordering.domain.dto.response.SalesResponseDto;
import com.server.ordering.domain.dto.response.RestaurantPreviewWithDistanceDto;
import com.server.ordering.domain.member.Owner;
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

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    public Optional<Long> registerRestaurant(Restaurant restaurant, Long ownerId) throws PersistenceException {
        restaurantRepository.save(restaurant);
        Owner owner = ownerRepository.findOne(ownerId);
        owner.registerRestaurant(restaurant);
        return Optional.of(restaurant.getId());
    }

    @Transactional
    public void putRestaurant(Long restaurantId, RestaurantInfoDto dto) throws PersistenceException {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        restaurant.putRestaurant(dto.getRestaurantName(), dto.getOwnerName(), dto.getAddress(),
                dto.getTableCount(), dto.getFoodCategory(), dto.getRestaurantType());
    }

    @Transactional(readOnly = true)
    public List<SalesResponseDto> getDailySalesOfRestaurant(Long restaurantId, String yearAndMonth) {
        return orderRepository.getDailySales(restaurantId, yearAndMonth);
    }

    @Transactional(readOnly = true)
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

    @Transactional
    public Boolean addRepresentativeMenu(Long restaurantId, Long foodId) {
        Restaurant restaurant = restaurantRepository.findOneWithRepresentativeMenu(restaurantId);
        Boolean ableToAddRepresentativeMenu = restaurant.isAbleToAddRepresentativeMenu();
        if (ableToAddRepresentativeMenu) {
            Food food = foodRepository.findOne(foodId);
            String id = String.format("%d%d", restaurantId, foodId);
            RepresentativeMenu representativeMenu = new RepresentativeMenu(id, restaurant, food, food.getFoodName());
            restaurant.addRepresentativeMenu(representativeMenu);
            representativeMenuRepository.save(representativeMenu);
            return true;
        }
        return false;
    }

    @Transactional
    public void removeRepresentativeMenu(Long restaurantId, Long foodId) {
        String id = String.format("%d%d", restaurantId, foodId);
        representativeMenuRepository.remove(id);
    }

    @Transactional
    public List<RestaurantPreviewWithDistanceDto> getAllForPreview(RestaurantPreviewListReqDto dto) {
        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);

        // 사용자의 위도, 경도 값으로 2차원 좌표 생성
        Point point = factory.createPoint(new Coordinate(dto.getLongitude(), dto.getLatitude()));
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
}
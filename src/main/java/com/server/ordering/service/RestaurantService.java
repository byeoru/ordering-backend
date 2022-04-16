package com.server.ordering.service;

import com.server.ordering.S3Service;
import com.server.ordering.domain.Food;
import com.server.ordering.domain.RepresentativeMenu;
import com.server.ordering.domain.Restaurant;
import com.server.ordering.domain.dto.FoodDto;
import com.server.ordering.domain.dto.request.RestaurantInfoDto;
import com.server.ordering.domain.dto.response.DailySalesDto;
import com.server.ordering.domain.member.Owner;
import com.server.ordering.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Map;
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
        owner.setRestaurant(restaurant);
        return Optional.of(restaurant.getId());
    }

    @Transactional
    public void putRestaurant(Long restaurantId, RestaurantInfoDto dto) throws PersistenceException {
        restaurantRepository.put(dto.getRestaurantName(), dto.getOwnerName(), dto.getAddress(),
                dto.getTableCount(), dto.getFoodCategory().toString(), dto.getRestaurantType().toString(), restaurantId);
    }

    public List<DailySalesDto> getMonthlySalesOfRestaurant(Long restaurantId, String from, String before) {
        return orderRepository.getMonthlySales(restaurantId, from, before);
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
    public List<Restaurant> getAllForPreview() {
        return restaurantRepository.findAllWithRepresentativeMenu();
    }
}
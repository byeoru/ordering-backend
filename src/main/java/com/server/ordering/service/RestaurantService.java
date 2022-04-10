package com.server.ordering.service;

import com.server.ordering.domain.Food;
import com.server.ordering.domain.Restaurant;
import com.server.ordering.domain.dto.FoodDto;
import com.server.ordering.domain.dto.request.RestaurantInfoDto;
import com.server.ordering.domain.dto.response.DailySalesDto;
import com.server.ordering.domain.member.Owner;
import com.server.ordering.repository.FoodRepository;
import com.server.ordering.repository.OrderRepository;
import com.server.ordering.repository.OwnerRepository;
import com.server.ordering.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final OwnerRepository ownerRepository;

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
}
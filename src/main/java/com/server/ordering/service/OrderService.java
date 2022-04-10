package com.server.ordering.service;

import com.server.ordering.domain.Food;
import com.server.ordering.domain.Order;
import com.server.ordering.domain.OrderFood;
import com.server.ordering.domain.Restaurant;
import com.server.ordering.domain.dto.request.OrderFoodDto;
import com.server.ordering.domain.dto.request.OrderDto;
import com.server.ordering.domain.member.Customer;
import com.server.ordering.repository.CustomerRepository;
import com.server.ordering.repository.FoodRepository;
import com.server.ordering.repository.OrderRepository;
import com.server.ordering.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void testSave(Order order) {
        orderRepository.save(order);
    }

    @Transactional
    public Long order(Long customerId, Long restaurantId, OrderDto orderDto) {
        Customer customer = customerRepository.findOne(customerId);
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        List<OrderFoodDto> orderFoodDtos = orderDto.getOrderFoodDtos();

        List<OrderFood> orderFoods = orderFoodDtos.stream().map(orderFoodDto -> {
            Food food = foodRepository.findOne(orderFoodDto.getFoodId());
            return OrderFood.createOrderFood(food, orderFoodDto.getPrice(), orderFoodDto.getCount());
        }).collect(Collectors.toList());
        Order order = Order.createOrder(customer, restaurant, orderFoods, orderDto.getOrderType(),
                orderDto.getTableNumber(), orderDto.getTotalPrice());

        orderRepository.save(order);
        return order.getId();
    }

    @Transactional
    public Boolean orderCancel(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        if (order.isAbleToCancel()) {
            order.cancel();
            return true;
        }
        return false;
    }
}

package com.server.ordering.service;

import com.server.ordering.domain.*;
import com.server.ordering.domain.dto.request.BasketDto;
import com.server.ordering.domain.dto.request.OrderDto;
import com.server.ordering.domain.member.Customer;
import com.server.ordering.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;
    private final OrderRepository orderRepository;
    private final BasketRepository basketRepository;

    @Transactional
    public void testSave(Order order) {
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Boolean isAbleToAddToBasket(Long customerId, Long restaurantId) {
        Customer customer = customerRepository.findOne(customerId);
        Long basketKey = customer.getBasketKey();
        return basketKey == null || basketKey.equals(restaurantId);
    }

    @Transactional
    public void addToBasket(Long customerId, Long restaurantId, Long foodId, BasketDto basketDto) {
        Customer customer = customerRepository.findOne(customerId);
        Food food = foodRepository.findOne(foodId);
        Basket basket = Basket.CreateBasket(customer, food, basketDto.getPrice(), basketDto.getCount());
        basketRepository.save(basket);
        if (customer.getBasketKey() == null) {
            customer.changeBasketKey(restaurantId);
        }
    }

    @Transactional
    public void removeToBasket(Long basketId, Long customerId) {
        basketRepository.remove(basketId);
        Customer customer = customerRepository.findOneWithBasket(customerId);
        int basketCount = customer.getBaskets().size();
        if (basketCount <= 0) {
            customer.changeBasketKey(null);
        }
    }

    @Transactional
    public void removeAllToBasket(Long customerId) {
        basketRepository.removeAll(customerId);
    }

    @Transactional
    public Long order(Long customerId, OrderDto orderDto) {
        int totalPrice = 0;
        Customer customer = customerRepository.findOneWithBasket(customerId);
        List<OrderFood> orderFoods = new ArrayList<>();
        for (Basket basket : customer.getBaskets()) {
            totalPrice += basket.getPrice() * basket.getCount();
            OrderFood orderFood = OrderFood.createOrderFood(basket.getFood(), basket.getPrice(), basket.getCount());
            orderFoods.add(orderFood);
        }

        Restaurant restaurant = restaurantRepository.findOne(customer.getBasketKey());
        Order order = Order.createOrder(customer, restaurant, orderFoods, orderDto.getOrderType(),
                orderDto.getTableNumber(), totalPrice);

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

    @Transactional
    public Boolean setOrderToCompleted(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        if (order.isAbleToCompleted()) {
            order.completed();
            order.registerOrderTime();
            return true;
        }
        return false;
    }
}

package com.server.ordering.service;

import com.server.ordering.FirebaseCloudMessageService;
import com.server.ordering.domain.*;
import com.server.ordering.domain.dto.request.BasketPutDto;
import com.server.ordering.domain.dto.request.BasketRequestDto;
import com.server.ordering.domain.dto.request.OrderDto;
import com.server.ordering.domain.member.Customer;
import com.server.ordering.exception.FcmErrorException;
import com.server.ordering.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.io.IOException;
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
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @Transactional
    public void testSave(Order order) {
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Boolean isAbleToAddToBasket(Long customerId, Long restaurantId) {
        Customer customer = customerRepository.findOne(customerId);

        // 직전에 장바구니에 저장한 음식의 음식점 PK
        Long basketKey = customer.getBasketKey();

        // 고객의 장바구니에 아무 것도 담겨있지 않거나 또는 같은 음식점의 음식을 담으려고 하는 경우 TRUE 반환
        return basketKey == null || basketKey.equals(restaurantId);
    }

    @Transactional
    public void addToBasket(Long customerId, Long restaurantId, BasketRequestDto basketDto) {
        Customer customer = customerRepository.findOne(customerId);
        Food food = foodRepository.findOne(basketDto.getFoodId());
        Basket basket = Basket.CreateBasket(customer, food, basketDto.getCount());

        // 장바구니에 저장
        basketRepository.save(basket);

        // basket_key 를 현재 장바구니에 저장한 음식의 음식점 PK로 UPDATE
        customer.changeBasketKey(restaurantId);
    }

    @Transactional
    public void removeToBasket(Long basketId, Long customerId) {
        basketRepository.remove(basketId);
        Customer customer = customerRepository.findOneWithBasket(customerId);
        int basketCount = customer.getBaskets().size();

        // 장바구니에 담긴 음식이 없으면
        if (basketCount <= 0) {
            customer.changeBasketKey(null);
        }
    }

    @Transactional
    public void putFoodCntInBasket(Long customerId, List<BasketPutDto> basketRequestDtos) {
        basketRepository.putCount(customerId, basketRequestDtos);
    }

    @Transactional
    public void removeAllToBasket(Long customerId) {
        basketRepository.removeAll(customerId);
    }

    @Transactional
    public Long order(Long customerId, OrderDto orderDto) throws FcmErrorException {
        int totalPrice = 0; // 총 주문 금액
        Customer customer = customerRepository.findOneWithBasketWithFood(customerId);
        List<OrderFood> orderFoods = new ArrayList<>();
        StringBuilder messageBuilder = new StringBuilder(customer.getBaskets().size() * 30);

        // 장바구에 담겨있는 음식들
        for (Basket basket : customer.getBaskets()) {
            totalPrice += basket.getFood().getPrice() * basket.getCount();
            OrderFood orderFood = OrderFood.createOrderFood(basket.getFood(), basket.getFood().getPrice(), basket.getCount());
            orderFoods.add(orderFood);

            // FCM message 추가
            messageBuilder.append(String.format("%s: %d개, ", basket.getFood().getFoodName(), basket.getCount()));
        }

        // FCM message 추가
        messageBuilder.append(String.format("총 주문 금액: %d원", totalPrice));

        Restaurant restaurant = restaurantRepository.findOneWithOwner(customer.getBasketKey());
        String token = restaurant.getOwner().getFirebaseToken();

        // 주문 생성
        Order order = Order.createOrder(customer, restaurant, orderFoods, orderDto.getOrderType(),
                orderDto.getTableNumber(), totalPrice);

        orderRepository.save(order);

        try {
            // 점주에게 FCM 발신
            firebaseCloudMessageService.sendMessageTo(token, "(주문접수) 주문이 접수되었습니다.", messageBuilder.toString());
        } catch (IOException e) {
            throw new FcmErrorException();
        }

        return order.getId();
    }

    @Transactional
    public Boolean orderCancel(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        if (order.isAbleToCancel()) {
            order.cancel();
            order.removeAllOrderFood();
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean orderOwnerCancel(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        if (order.isAbleToOwnerCancel()) {
            order.cancel();
            order.removeAllOrderFood();
            return true;
        }
        return false;
    }

    // 점주 side 주문 체크
    @Transactional
    public Boolean orderCheck(Long orderId) {
        Order order = orderRepository.findOneWithCustomer(orderId);
        if (order.isAbleToCheck()) {
            order.check();
            order.registerOrderTime();
            return true;
        }
        return false;
    }

    // 주문 완료
    @Transactional
    public Boolean orderComplete(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        if (order.isAbleToComplete()) {
            order.complete();
            return true;
        }
        return false;
    }

    public Basket findBasketByCustomerIdAndFoodId(Long customerId, Long foodId) {
        try {
            return basketRepository.findOneByCustomerIdAndFoodId(customerId, foodId);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public void updateBasketCount(Basket basket, BasketRequestDto basketRequestDto) {
        // 음식 수량만 증가(원래 수량 + 요청 수량)
        basket.updateCount(basket.getCount() + basketRequestDto.getCount());
    }
}

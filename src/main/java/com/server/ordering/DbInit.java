package com.server.ordering;

import com.server.ordering.domain.*;
import com.server.ordering.domain.dto.FoodDto;
import com.server.ordering.domain.dto.request.OrderDto;
import com.server.ordering.domain.dto.request.OrderFoodDto;
import com.server.ordering.domain.dto.response.DailySalesDto;
import com.server.ordering.domain.member.Customer;
import com.server.ordering.domain.member.Owner;
import com.server.ordering.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DbInit {

    private final OwnerService ownerService;
    private final CustomerService customerService;
    private final RestaurantService restaurantService;
    private final FoodService foodService;
    private final OrderService orderService;

    @PostConstruct
    public void init() {

        Customer customer = new Customer("벼루", "byeoru", "1234", new PhoneNumber("43829342", MemberType.CUSTOMER));
        customerService.signUp(customer);

        Owner byeongGyu = new Owner("byeoru", "1234", new PhoneNumber("12343785", MemberType.OWNER));
        ownerService.signUp(byeongGyu);

        Owner seongGyu = new Owner("asdasd", "asdasd", new PhoneNumber("4326986", MemberType.OWNER));
        ownerService.signUp(seongGyu);

        Owner minju = new Owner("minju", "minjuworld", new PhoneNumber("57896239", MemberType.OWNER));
        ownerService.signUp(minju);

        Owner asdasdasd = new Owner("asdasdasd", "asdasdasd", new PhoneNumber("01073868114", MemberType.OWNER));
        ownerService.signUp(asdasdasd);

        Owner mmmmmm = new Owner("mmmmmm", "mmmmmm", new PhoneNumber("01028171525", MemberType.OWNER));
        ownerService.signUp(mmmmmm);

        Restaurant restaurant1 = new Restaurant("abcabc", "ㄷㄷㄷ",
                "52828, 우 우리 우리집 501 (가좌동), 내방", 10, FoodCategory.PIZZA, RestaurantType.FOR_HERE_TO_GO);
        Restaurant restaurant2 = new Restaurant("abcabc", "ㄷㄷㄷ",
                "52828, 우 우리 우리집 501 (가좌동), 내방", 10, FoodCategory.PIZZA, RestaurantType.FOR_HERE_TO_GO);
        Restaurant restaurant3 = new Restaurant("abcabc", "ㄷㄷㄷ",
                "52828, 우 우리 우리집 501 (가좌동), 내방", 10, FoodCategory.PIZZA, RestaurantType.FOR_HERE_TO_GO);
        Restaurant restaurant4 = new Restaurant("abcabc", "ㄷㄷㄷ",
                "52828, 우 우리 우리집 501 (가좌동), 내방", 10, FoodCategory.PIZZA, RestaurantType.FOR_HERE_TO_GO);
        Restaurant restaurant5 = new Restaurant("abcabc", "ㄷㄷㄷ",
                "52828, 우 우리 우리집 501 (가좌동), 내방", 10, FoodCategory.PIZZA, RestaurantType.FOR_HERE_TO_GO);
        restaurantService.registerRestaurant(restaurant1, byeongGyu.getId());
        restaurantService.registerRestaurant(restaurant2, seongGyu.getId());
        restaurantService.registerRestaurant(restaurant3, minju.getId());
        restaurantService.registerRestaurant(restaurant4, asdasdasd.getId());
        restaurantService.registerRestaurant(restaurant5, mmmmmm.getId());

        Order order1 = new Order(customer, LocalDateTime.of(2022, 3, 3, 9, 15),
                restaurant1, OrderStatus.ORDERED, OrderType.TABLE_ORDER, 3, 3240);
        orderService.testSave(order1);

        Order order2 = new Order(customer, LocalDateTime.of(2022, 4, 1, 10, 1),
                restaurant1, OrderStatus.ORDERED, OrderType.TABLE_ORDER, 3, 35000);
        orderService.testSave(order2);

        Order order3 = new Order(customer, LocalDateTime.of(2022, 4, 3, 12, 1),
                restaurant1, OrderStatus.ORDERED, OrderType.TABLE_ORDER, 3, 35000);
        orderService.testSave(order3);

        Order order4 = new Order(customer, LocalDateTime.of(2022, 4, 3, 10, 1),
                restaurant1, OrderStatus.ORDERED, OrderType.TABLE_ORDER, 3, 35000);
        orderService.testSave(order4);

        Order order5 = new Order(customer, LocalDateTime.of(2022, 4, 13, 10, 1),
                restaurant1, OrderStatus.ORDERED, OrderType.TABLE_ORDER, 3, 35000);
        orderService.testSave(order5);

        Order order6 = new Order(customer, LocalDateTime.of(2022, 4, 13, 10, 1),
                restaurant1, OrderStatus.ORDERED, OrderType.TABLE_ORDER, 3, 4000);
        orderService.testSave(order6);

        Order order7 = new Order(customer, LocalDateTime.of(2022, 5, 5, 10, 1),
                restaurant1, OrderStatus.ORDERED, OrderType.TABLE_ORDER, 3, 5000);
        orderService.testSave(order7);

        Order order8 = new Order(customer, LocalDateTime.of(2023, 4, 5, 12, 1),
                restaurant1, OrderStatus.ORDERED, OrderType.TABLE_ORDER, 3, 5000);
        orderService.testSave(order8);

        watchSales();
    }

    private void watchSales() {
        List<DailySalesDto> monthlySalesOfRestaurant = restaurantService.getMonthlySalesOfRestaurant(1L, "2022-04", "2022-05");

    }
}

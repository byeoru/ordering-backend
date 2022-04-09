package com.server.ordering;

import com.server.ordering.domain.*;
import com.server.ordering.domain.member.Customer;
import com.server.ordering.domain.member.Owner;
import com.server.ordering.service.CustomerService;
import com.server.ordering.service.OwnerService;
import com.server.ordering.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DbInit {

    private final OwnerService ownerService;
    private final CustomerService customerService;
    private final RestaurantService restaurantService;

    @PostConstruct
    public void init() {
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

        Customer customer = new Customer("벼루", "byeoru", "1234", new PhoneNumber("43829342", MemberType.CUSTOMER));
        customerService.signUp(customer);
    }
}

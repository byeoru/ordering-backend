package com.server.ordering;

import com.server.ordering.api.RestaurantApiController;
import com.server.ordering.domain.*;
import com.server.ordering.domain.dto.FoodDto;
import com.server.ordering.domain.dto.request.CouponDto;
import com.server.ordering.domain.member.Customer;
import com.server.ordering.domain.member.Owner;
import com.server.ordering.service.*;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DbInit {

//    private final OwnerService ownerService;
//    private final CustomerService customerService;
//    private final RestaurantService restaurantService;
//    private final CouponService couponService;
//    private final FoodService foodService;
//
//    @PostConstruct
//    public void init() {
//
//        Customer customer = new Customer("벼루", "byeoru", "1234", new PhoneNumber("43829342", MemberType.CUSTOMER));
//        customerService.signUp(customer);
//
//        Owner byeongGyu = new Owner("byeoru", "1234", new PhoneNumber("12343785", MemberType.OWNER));
//        ownerService.signUp(byeongGyu);
//
//        Owner seongGyu = new Owner("asdasd", "asdasd", new PhoneNumber("4326986", MemberType.OWNER));
//        ownerService.signUp(seongGyu);
//
//        Owner minju = new Owner("minju", "minjuworld", new PhoneNumber("57896239", MemberType.OWNER));
//        ownerService.signUp(minju);
//
//        Owner mmmmmm = new Owner("mmmmmm", "mmmmmm", new PhoneNumber("01028171525", MemberType.OWNER));
//        ownerService.signUp(mmmmmm);
//
//        double latitude = 35.1537515937909;
//        double longitude = 128.107165988155;
//        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
//        Point point = factory.createPoint(new Coordinate(longitude,latitude));
//
//        Restaurant restaurant1 = new Restaurant("abcabc", "ㄷㄷㄷ",
//                "52828, 우 우리 우리집 501 (가좌동), 내방", point, 10, FoodCategory.PIZZA, RestaurantType.FOR_HERE_TO_GO);
//
//        Restaurant restaurant2 = new Restaurant("반반스프링스 경상대점", "박승규",
//                "52827, 경남 진주시 가좌안골길6번길 1 (가좌동), 1층 반반스프링스커피", point,30, FoodCategory.CAFE_DESSERT, RestaurantType.FOR_HERE_TO_GO);
//
//        Restaurant restaurant3 = new Restaurant("abcabc", "ㄷㄷㄷ",
//                "52828, 우 우리 우리집 501 (가좌동), 내방", point,10, FoodCategory.PIZZA, RestaurantType.FOR_HERE_TO_GO);
//        Restaurant restaurant4 = new Restaurant("abcabc", "ㄷㄷㄷ",
//                "52828, 우 우리 우리집 501 (가좌동), 내방", point,10, FoodCategory.PIZZA, RestaurantType.FOR_HERE_TO_GO);
//        Restaurant restaurant5 = new Restaurant("abcabc", "ㄷㄷㄷ",
//                "52828, 우 우리 우리집 501 (가좌동), 내방", point,10, FoodCategory.PIZZA, RestaurantType.FOR_HERE_TO_GO);
//
//        restaurantService.registerRestaurant(restaurant1, byeongGyu.getId());
//        restaurantService.registerRestaurant(restaurant2, seongGyu.getId());
//        restaurantService.registerRestaurant(restaurant3, minju.getId());
//        restaurantService.registerRestaurant(restaurant5, mmmmmm.getId());
//
//        FoodDto foodDto1 = new FoodDto();
//        foodDto1.setFoodName("아메리카노(ICE)");
//        foodDto1.setPrice(3900);
//        foodDto1.setSoldOut(false);
//        foodDto1.setMenuIntro("여름엔~~ 아이스 아메리카노 여름엔~~ 반반아이스");
//
//        FoodDto foodDto2 = new FoodDto();
//        foodDto2.setFoodName("쿠키앤크림프라페(ICE)");
//        foodDto2.setPrice(5800);
//        foodDto2.setSoldOut(false);
//        foodDto2.setMenuIntro("따뜻한 프라페를 원하시면 말씀하세요~ ㅋㅅㅋ");
//
//        FoodDto foodDto3 = new FoodDto();
//        foodDto3.setFoodName("허니브레드");
//        foodDto3.setPrice(5100);
//        foodDto3.setSoldOut(false);
//        foodDto3.setMenuIntro("꿀벌도 울고 갈 달콤함");
//
//        FoodDto foodDto4 = new FoodDto();
//        foodDto4.setFoodName("헤이즐넛아메리카노(ICE)");
//        foodDto4.setPrice(4400);
//        foodDto4.setSoldOut(false);
//        foodDto4.setMenuIntro("헤이즈가 극찬했어요");
//
//        FoodDto foodDto5 = new FoodDto();
//        foodDto5.setFoodName("카푸치노(ICE)");
//        foodDto5.setPrice(4600);
//        foodDto5.setSoldOut(false);
//        foodDto5.setMenuIntro("니좀치노?");
//
//        FoodDto foodDto6 = new FoodDto();
//        foodDto6.setFoodName("카페라떼(ICE)");
//        foodDto6.setPrice(4600);
//        foodDto6.setSoldOut(false);
//        foodDto6.setMenuIntro("라떼는 말이야~ 할 줄 알았죠? 안해요~");
//
//        FoodDto foodDto7 = new FoodDto();
//        foodDto7.setFoodName("바닐라라떼(ICE)");
//        foodDto7.setPrice(4600);
//        foodDto7.setSoldOut(false);
//        foodDto7.setMenuIntro("이거 마셨더니 원숭이들이 쫓아와요");
//
//        FoodDto foodDto8 = new FoodDto();
//        foodDto8.setFoodName("연유라떼(ICE)");
//        foodDto8.setPrice(4600);
//        foodDto8.setSoldOut(false);
//        foodDto8.setMenuIntro("오늘 반반 여나유~? ㅈㅅ");
//
//        FoodDto foodDto9 = new FoodDto();
//        foodDto9.setFoodName("아메리카노(ICE)");
//        foodDto9.setPrice(5200);
//        foodDto9.setSoldOut(false);
//        foodDto9.setMenuIntro("아 또 카라멜마끼야또?");
//
//        foodService.registerFood(restaurant2.getId(), foodDto1, null);
//        foodService.registerFood(restaurant2.getId(), foodDto2, null);
//        foodService.registerFood(restaurant2.getId(), foodDto3, null);
//        foodService.registerFood(restaurant2.getId(), foodDto4, null);
//        foodService.registerFood(restaurant2.getId(), foodDto5, null);
//        foodService.registerFood(restaurant2.getId(), foodDto6, null);
//        foodService.registerFood(restaurant2.getId(), foodDto7, null);
//        foodService.registerFood(restaurant2.getId(), foodDto8, null);
//        foodService.registerFood(restaurant2.getId(), foodDto9, null);
//
//        couponService.saveCoupon(new CouponDto("632224a7c23d486912388c0d9dbbe52b", 30000));
//        couponService.saveCoupon(new CouponDto("c3a22dcaeaff630eaaf14ac087eb6e2b", 50000));
//        couponService.saveCoupon(new CouponDto("d00491d25f351b000e45c3072935590d", 100000));
//    }
}

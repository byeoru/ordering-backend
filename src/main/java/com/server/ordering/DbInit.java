package com.server.ordering;

import com.server.ordering.domain.*;
import com.server.ordering.domain.dto.FoodDto;
import com.server.ordering.domain.dto.request.CouponDto;
import com.server.ordering.domain.dto.request.CustomerSignUpDto;
import com.server.ordering.domain.dto.request.OwnerSignUpDto;
import com.server.ordering.domain.dto.request.RestaurantDataWithLocationDto;
import com.server.ordering.service.*;
import lombok.RequiredArgsConstructor;
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
//        CustomerSignUpDto customer = new CustomerSignUpDto("벼루", "byeoru", "1234", "43829342");
//        customerService.signUp(customer);
//
//        OwnerSignUpDto byeongGyu = new OwnerSignUpDto("byeoru", "1234", "12343785");
//        Long byeongGyuId = ownerService.signUp(byeongGyu);
//
//        OwnerSignUpDto seongGyu = new OwnerSignUpDto("asdasd", "asdasd", "4326986");
//        Long seongGyuId = ownerService.signUp(seongGyu);
//
//        OwnerSignUpDto minju = new OwnerSignUpDto("minju", "minjuworld", "57896239");
//        Long minjuId = ownerService.signUp(minju);
//
//        double latitude = 35.1537515937909;
//        double longitude = 128.107165988155;
//
//        RestaurantDataWithLocationDto restaurant1 = new RestaurantDataWithLocationDto("abcabc", "ㄷㄷㄷ",
//                "52828, 우 우리 우리집 501 (가좌동), 내방", 10, FoodCategory.PIZZA, RestaurantType.FOR_HERE_TO_GO, 10, 2, latitude, longitude);
//
//        RestaurantDataWithLocationDto restaurant2 = new RestaurantDataWithLocationDto("반반스프링스 경상대점", "박승규",
//                "52827, 경남 진주시 가좌안골길6번길 1 (가좌동), 1층 반반스프링스커피", 30, FoodCategory.CAFE_DESSERT, RestaurantType.FOR_HERE_TO_GO, 10, 2, latitude, longitude);
//
//        RestaurantDataWithLocationDto restaurant3 = new RestaurantDataWithLocationDto("abcabc", "ㄷㄷㄷ",
//                "52828, 우 우리 우리집 501 (가좌동), 내방", 10, FoodCategory.PIZZA, RestaurantType.FOR_HERE_TO_GO, 10, 2, latitude, longitude);
//
//        Long restaurant1Id = restaurantService.registerRestaurant(byeongGyuId, restaurant1);
//        Long restaurant2Id = restaurantService.registerRestaurant(seongGyuId, restaurant2);
//        Long restaurant3Id = restaurantService.registerRestaurant(minjuId, restaurant3);
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
//        foodService.registerFood(restaurant1Id, foodDto1, null);
//
//        foodService.registerFood(restaurant2Id, foodDto1, null);
//        foodService.registerFood(restaurant2Id, foodDto2, null);
//        foodService.registerFood(restaurant2Id, foodDto3, null);
//        foodService.registerFood(restaurant2Id, foodDto4, null);
//        foodService.registerFood(restaurant2Id, foodDto5, null);
//        foodService.registerFood(restaurant2Id, foodDto6, null);
//        foodService.registerFood(restaurant2Id, foodDto7, null);
//        foodService.registerFood(restaurant2Id, foodDto8, null);
//        foodService.registerFood(restaurant2Id, foodDto9, null);
//
//        couponService.saveCoupon(new CouponDto("632224a7c23d486912388c0d9dbbe52b", 30000));
//        couponService.saveCoupon(new CouponDto("c3a22dcaeaff630eaaf14ac087eb6e2b", 50000));
//        couponService.saveCoupon(new CouponDto("d00491d25f351b000e45c3072935590d", 100000));
//    }
}

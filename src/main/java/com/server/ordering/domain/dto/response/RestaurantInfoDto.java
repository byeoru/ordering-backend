package com.server.ordering.domain.dto.response;

import com.server.ordering.domain.FoodCategory;
import com.server.ordering.domain.OrderType;
import com.server.ordering.domain.Restaurant;
import com.server.ordering.domain.RestaurantType;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class RestaurantInfoDto {

    private String ownerName;
    private String restaurantName;
    private String address;
    private String notice;
    private RestaurantType restaurantType;
    private FoodCategory foodCategory;
    private Integer tableCount;
    private Integer orderingWaitingTime;
    private Integer admissionWaitingTime;
    private double latitude;
    private double longitude;

    public RestaurantInfoDto(Restaurant restaurant) {
        this.ownerName = restaurant.getOwnerName();
        this.restaurantName = restaurant.getRestaurantName();
        this.address = restaurant.getAddress();
        this.notice = restaurant.getNotice();
        this.restaurantType = restaurant.getRestaurantType();
        this.foodCategory = restaurant.getFoodCategory();
        this.tableCount = restaurant.getTableCount();
        this.orderingWaitingTime = restaurant.getOrderingWaitingTime();
        this.admissionWaitingTime = restaurant.getAdmissionWaitingTime();
        this.latitude = restaurant.getLocation().getY();
        this.longitude = restaurant.getLocation().getX();
    }
}

//대표자명, 음식점명, 주소, 주문타입, 음식타입, 테이블수, 웨이팅 대기시간, 주문 대기시간
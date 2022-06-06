package com.server.ordering.domain.dto.response;

import com.server.ordering.domain.Waiting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class MyWaitingInfoDto {

    private Long waitingId;
    private Integer myWaitingNumber;
    private Long numInFrontOfMe;
    private int estimatedWaitingTime;
    private String waitingRegisterTime;
    private Long restaurantId;
    private String restaurantName;
    private String profileImageUrl;
    private String backgroundImageUrl;

    public MyWaitingInfoDto(Waiting waiting, long numberInFrontOfMe) {
        this.waitingId = waiting.getId();
        this.myWaitingNumber = waiting.getMyWaitingNumber();
        this.numInFrontOfMe = numberInFrontOfMe;
        this.estimatedWaitingTime = waiting.getRestaurant().getAdmissionWaitingTime() * ((int) numberInFrontOfMe + 1);
        this.waitingRegisterTime = waiting.getWaitingRegisterTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss"));
        this.restaurantId = waiting.getRestaurant().getId();
        this.restaurantName = waiting.getRestaurant().getRestaurantName();
        this.profileImageUrl = waiting.getRestaurant().getProfileImageUrl();
        this.backgroundImageUrl = waiting.getRestaurant().getBackgroundImageUrl();
    }
}

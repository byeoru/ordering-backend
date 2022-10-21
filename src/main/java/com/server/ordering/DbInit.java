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

    private final OwnerService ownerService;
    private final CustomerService customerService;

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
//    }
}

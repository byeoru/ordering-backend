package com.byeoru.ordering_server

import com.byeoru.ordering_server.service.CustomerService
import com.byeoru.ordering_server.service.OwnerService
import org.springframework.stereotype.Component

//@Component
//class DbInit     //    @PostConstruct
//    (private val ownerService: OwnerService, private val customerService: CustomerService) {
//    //    public void init() {
//    //
//    //        CustomerSignUpDto customer = new CustomerSignUpDto("벼루", "byeoru", "1234", "43829342");
//    //        customerService.signUp(customer);
//    //
//    //        OwnerSignUpDto byeongGyu = new OwnerSignUpDto("byeoru", "1234", "12343785");
//    //        Long byeongGyuId = ownerService.signUp(byeongGyu);
//    //
//    //        OwnerSignUpDto seongGyu = new OwnerSignUpDto("asdasd", "asdasd", "4326986");
//    //        Long seongGyuId = ownerService.signUp(seongGyu);
//    //
//    //        OwnerSignUpDto minju = new OwnerSignUpDto("minju", "minjuworld", "57896239");
//    //        Long minjuId = ownerService.signUp(minju);
//    //    }
//}
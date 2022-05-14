package com.server.ordering.service;

import com.server.ordering.domain.Restaurant;
import com.server.ordering.domain.Waiting;
import com.server.ordering.domain.member.Customer;
import com.server.ordering.repository.CustomerRepository;
import com.server.ordering.repository.RestaurantRepository;
import com.server.ordering.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class WaitingService {

    private final RestaurantRepository restaurantRepository;
    private final WaitingRepository waitingRepository;
    private final CustomerRepository customerRepository;

    /**
     * 웨이팅 등록
     */
    @Transactional
    public void registerWaiting(Long restaurantId, Long customerId, Byte peopleNumber) {
        Restaurant restaurant = restaurantRepository.findOneLock(restaurantId);
        Customer customer = customerRepository.findOne(customerId);
        restaurant.addWaitingCnt();
        Integer myWaitingNumber = restaurant.getWaitingTotalCount();
        Waiting waiting = new Waiting(restaurant, customer, myWaitingNumber, peopleNumber);
        waitingRepository.save(waiting);
    }

    /**
     * 웨이팅 취소
     */
    @Transactional
    public void cancelWaiting(Long waitingId) {
        waitingRepository.remove(waitingId);
    }

    /**
     * 고객 PK로 웨이팅 조회
     */
    public Waiting findOneFromCustomer(Long customerId) {
        return waitingRepository.findOne(customerId);
    }

    /**
     * 내 앞 대기 인원 수 조회
     */
    public Long getNumberInFrontOfMe(Integer myWaitingNumber, Long restaurantId) {
        return waitingRepository.getCountInFrontOfMe(myWaitingNumber, restaurantId);
    }
}

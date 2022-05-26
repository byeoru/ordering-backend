package com.server.ordering.service;

import com.server.ordering.domain.Restaurant;
import com.server.ordering.domain.Waiting;
import com.server.ordering.domain.dto.request.WaitingRegisterDto;
import com.server.ordering.domain.member.Customer;
import com.server.ordering.repository.CustomerRepository;
import com.server.ordering.repository.RestaurantRepository;
import com.server.ordering.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

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
    public void registerWaiting(Long restaurantId, Long customerId, WaitingRegisterDto dto) {

        // 여러 스레드의 접근으로 동시성 문제가 발생할 수 있으므로 낙관적 Lock을 걸고 가져온다.
        Restaurant restaurant = restaurantRepository.findOneLock(restaurantId);
        Customer customer = customerRepository.findOneWithPhoneNumber(customerId);

        // Lock 을 적용한 상태에서 해당 음식점 waiting count 증가
        restaurant.addWaitingCnt();

        // 증가된 count 가 내 waiting 번호
        Integer myWaitingNumber = restaurant.getWaitingCount();

        Waiting waiting = new Waiting(restaurant, customer, myWaitingNumber,
                dto.getNumOfTeamMembers(), customer.getPhoneNumber());

        // 웨이팅 등록시간 설정
        waiting.registerWaitingTime();
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
    public Waiting findOneWithRestaurantByCustomer(Long customerId) {
        try {
            return waitingRepository.findOneWithRestaurant(customerId);
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 내 앞 대기 인원 수 조회
     */
    @Transactional(readOnly = true)
    public Long getNumberInFrontOfMe(Integer myWaitingNumber, Long restaurantId) {
        return waitingRepository.getCountInFrontOfMe(myWaitingNumber, restaurantId);
    }

    /**
     * 음식점 웨이팅 리스트 조회
     */
    public List<Waiting> findAllWithPhoneNumberRestaurantWaiting(Long restaurantId) {
        return waitingRepository.findAllWithPhoneNumberByRestaurantId(restaurantId);
    }
}

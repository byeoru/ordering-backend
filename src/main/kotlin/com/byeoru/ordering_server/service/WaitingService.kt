package com.byeoru.ordering_server.service

import com.byeoru.ordering_server.FirebaseCloudMessageService
import com.byeoru.ordering_server.domain.OrderType
import com.byeoru.ordering_server.domain.Waiting
import com.byeoru.ordering_server.domain.dto.request.WaitingRegisterDto
import com.byeoru.ordering_server.repository.CustomerRepository
import com.byeoru.ordering_server.repository.RestaurantRepository
import com.byeoru.ordering_server.repository.WaitingRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.time.format.DateTimeFormatter
import javax.persistence.NoResultException

@Service
@Transactional(readOnly = true)
class WaitingService(private val restaurantRepository: RestaurantRepository,
                     private val waitingRepository: WaitingRepository,
                     private val customerRepository: CustomerRepository,
                     private val firebaseCloudMessageService: FirebaseCloudMessageService) {

    /**
     * 웨이팅 등록
     */
    @Transactional
    fun registerWaiting(restaurantId: Long, customerId: Long, dto: WaitingRegisterDto) {

        // 여러 스레드의 접근으로 동시성 문제가 발생할 수 있으므로 낙관적 Lock을 걸고 가져온다.
        val restaurant = restaurantRepository.findOneLock(restaurantId)
        val customer = customerRepository.findOne(customerId)

        // Lock 을 적용한 상태에서 해당 음식점 waiting count 증가
        restaurant.increaseWaitingCount()

        // 증가된 count 가 내 waiting 번호
        val myWaitingNumber = restaurant.waitingCount
        val waiting = Waiting(
            restaurant, customer, myWaitingNumber,
            dto.numOfTeamMembers, customer.phoneNumber
        )

        // 웨이팅 등록시간 설정
        waiting.registerWaitingTime()
        waitingRepository.save(waiting)
        val firebaseToken = restaurant.owner!!.firebaseToken!!
        val message = String.format(
            "[%s] 인원 : %d명의 웨이팅 팀이 새롭게 추가되었습니다.",
            waiting.waitingRegisterTime!!.format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss")),
            waiting.numOfTeamMembers
        )
        try {
            firebaseCloudMessageService.sendMessageTo(
                firebaseToken, "(웨이팅 접수) 새로운 웨이팅이 접수되었습니다.",
                message, OrderType.WAITING
            )
        } catch (e: IOException) {
            throw Exception()
        }
    }

    /**
     * 웨이팅 취소 or 삭제
     */
    @Transactional
    fun cancelOrDeleteWaiting(waitingId: Long) = waitingRepository.remove(waitingId)

    /**
     * 고객 PK로 웨이팅 조회
     */
    fun findOneWithRestaurantByCustomer(customerId: Long): Waiting? {
        return try {
            waitingRepository.findOneWithRestaurant(customerId)
        } catch (e: NoResultException) {
            null
        }
    }

    /**
     * 내 앞 대기 인원 수 조회
     */
    fun getNumberInFrontOfMe(myWaitingNumber: Int, restaurantId: Long) =
        waitingRepository.getCountInFrontOfMe(myWaitingNumber, restaurantId)

    /**
     * 음식점 웨이팅 리스트 조회
     */
    fun findAllWithPhoneNumberRestaurantWaiting(restaurantId: Long) =
        waitingRepository.findAllWithPhoneNumberByRestaurantId(restaurantId)

    companion object {
        private val log = LoggerFactory.getLogger(WaitingService::class.java)
    }
}
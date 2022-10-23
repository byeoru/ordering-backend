package com.byeoru.ordering_server.service

import com.byeoru.ordering_server.FirebaseCloudMessageService
import com.byeoru.ordering_server.S3Service
import com.byeoru.ordering_server.domain.OrderType
import com.byeoru.ordering_server.domain.RepresentativeMenu
import com.byeoru.ordering_server.domain.Restaurant
import com.byeoru.ordering_server.domain.dto.request.MessageDto
import com.byeoru.ordering_server.domain.dto.request.RestaurantDataWithLocationDto
import com.byeoru.ordering_server.domain.dto.request.RestaurantPreviewListReqDto
import com.byeoru.ordering_server.domain.dto.request.WaitingTimeDto
import com.byeoru.ordering_server.domain.dto.response.RestaurantPreviewWithDistanceDto
import com.byeoru.ordering_server.repository.*
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.PrecisionModel
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import javax.persistence.NoResultException
import javax.persistence.PersistenceException

@Service
@Transactional(readOnly = true)
class RestaurantService(private val s3Service: S3Service,
                        private val restaurantRepository: RestaurantRepository,
                        private val orderRepository: OrderRepository,
                        private val ownerRepository: OwnerRepository,
                        private val foodRepository: FoodRepository,
                        private val representativeMenuRepository: RepresentativeMenuRepository,
                        private val reviewRepository: ReviewRepository,
                        private val waitingRepository: WaitingRepository,
                        private val firebaseCloudMessageService: FirebaseCloudMessageService) {

    @Transactional
    @Throws(PersistenceException::class)
    fun registerRestaurant(ownerId: Long, dto: RestaurantDataWithLocationDto): Long? {
        val factory = GeometryFactory(PrecisionModel(), 4326)
        // client 에서 보낸 좌표값으로 Point Type 객체 생성
        val point = factory.createPoint(Coordinate(dto.longitude, dto.latitude))
        val restaurant = Restaurant(
            dto.restaurantName, dto.ownerName, dto.address, point,
            dto.tableCount, dto.foodCategory, dto.restaurantType,
            dto.orderingWaitingTime, dto.admissionWaitingTime
        )
        restaurantRepository.save(restaurant)
        val owner = ownerRepository.findOne(ownerId)
        owner.registerRestaurant(restaurant)
        return restaurant.id
    }

    @Transactional
    @Throws(PersistenceException::class)
    fun putRestaurant(restaurantId: Long, dto: RestaurantDataWithLocationDto) {
        val factory = GeometryFactory(PrecisionModel(), 4326)
        val point = factory.createPoint(Coordinate(dto.longitude, dto.latitude))
        val restaurant = restaurantRepository.findOne(restaurantId)
        restaurant.putRestaurant(
            dto.restaurantName, dto.ownerName, dto.address, point,
            dto.tableCount, dto.foodCategory, dto.restaurantType
        )
    }

    fun getDailySalesOfRestaurant(restaurantId: Long, yearAndMonth: String) =
        orderRepository.getDailySales(restaurantId, yearAndMonth)

    fun getMonthlySalesOfRestaurant(restaurantId: Long, year: String) =
        orderRepository.getMonthlySales(restaurantId, year)

    @Transactional
    fun putRestaurantProfileImage(restaurantId: Long, image: MultipartFile) {
        val imageKey = restaurantId.toString() + "restaurant-profile" + System.currentTimeMillis()
        val imageUrl: String = s3Service.upload(image, imageKey)
        val restaurant = restaurantRepository.findOne(restaurantId)
        restaurant.putProfileImageUrl(imageUrl)
    }

    @Transactional
    fun putRestaurantBackgroundImage(restaurantId: Long, image: MultipartFile) {
        val imageKey = restaurantId.toString() + "restaurant-background" + System.currentTimeMillis()
        val imageUrl: String = s3Service.upload(image, imageKey)
        val restaurant = restaurantRepository.findOne(restaurantId)
        restaurant.putBackgroundImageUrl(imageUrl)
    }

    fun isExistRepresentativeMenu(foodId: Long): Boolean {
        return try {
            representativeMenuRepository.findOneByFoodId(foodId)
            true
        } catch (e: NoResultException) {
            false
        }
    }

    @Transactional
    fun addRepresentativeMenu(restaurantId: Long, foodId: Long) {
        val restaurant = restaurantRepository.findOne(restaurantId)
        val food = foodRepository.findOne(foodId)
        val representativeMenu = RepresentativeMenu(restaurant, food, food.foodName)
        restaurant.addRepresentativeMenu(representativeMenu)
        representativeMenuRepository.save(representativeMenu)
    }

    fun findAllRepresentative(restaurantId: Long) =
        representativeMenuRepository.findAllByRestaurantId(restaurantId)

    @Transactional
    fun removeRepresentativeMenu(representativeMenuId: Long) =
        representativeMenuRepository.remove(representativeMenuId)

    @Transactional
    fun getAllForPreview(dto: RestaurantPreviewListReqDto): List<RestaurantPreviewWithDistanceDto> {
        val factory = GeometryFactory(PrecisionModel(), 4326)

        // 사용자의 위도, 경도 값으로 2차원 좌표 생성
        val point = factory.createPoint(Coordinate(dto.latitude, dto.longitude))
        return restaurantRepository.findAllWithRepresentativeMenu(point, dto.foodCategory)
    }

    @Transactional
    fun putOrderWaitingTime(restaurantId: Long, waitingTimeDto: WaitingTimeDto) {
        val restaurant = restaurantRepository.findOne(restaurantId)
        restaurant.putOrderWaitingTime(waitingTimeDto.minutes)
    }

    @Transactional
    fun putAdmissionWaitingTime(restaurantId: Long, waitingTimeDto: WaitingTimeDto) {
        val restaurant = restaurantRepository.findOne(restaurantId)
        restaurant.putAdmissionWaitingTime(waitingTimeDto.minutes)
    }

    fun findRestaurant(restaurantId: Long) = restaurantRepository.findOne(restaurantId)

    @Transactional
    fun putRestaurantNotice(restaurantId: Long, messageDto: MessageDto) {
        val restaurant = restaurantRepository.findOne(restaurantId)
        restaurant.putNotice(messageDto.message)
    }

    fun findReviewWithOrderWithCustomer(restaurantId: Long) =
        reviewRepository.findAllWithOrderWithCustomerByRestaurantId(restaurantId)

    fun callWaiting(waitingId: Long) {
        val waiting = waitingRepository.findOneWithCustomerAndRestaurant(waitingId)
        val token = waiting.customer.firebaseToken!!
        val title = String.format("(웨이팅 호출) %s", waiting.restaurant.restaurantName)
        val body = String.format("대기번호 %d번님, 매장 입장해주세요!", waiting.myWaitingNumber)
        try {
            // 고객에게 웨이팅 호출 FCM 발신
            firebaseCloudMessageService.sendMessageTo(token, title, body, OrderType.WAITING)
        } catch (e: IOException) {
            throw Exception()
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(RestaurantService::class.java)
    }
}
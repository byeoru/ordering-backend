package com.byeoru.ordering_server.service

import com.byeoru.ordering_server.FirebaseCloudMessageService
import com.byeoru.ordering_server.domain.Basket
import com.byeoru.ordering_server.domain.Order.Companion.createOrder
import com.byeoru.ordering_server.domain.OrderFood
import com.byeoru.ordering_server.domain.OrderFood.Companion.createOrderFood
import com.byeoru.ordering_server.domain.OrderType
import com.byeoru.ordering_server.domain.dto.request.BasketPutDto
import com.byeoru.ordering_server.domain.dto.request.BasketRequestDto
import com.byeoru.ordering_server.domain.dto.request.MessageDto
import com.byeoru.ordering_server.domain.dto.request.OrderDto
import com.byeoru.ordering_server.repository.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.time.format.DateTimeFormatter
import javax.persistence.NoResultException

@Service
@Transactional(readOnly = true)
class OrderService(private val customerRepository: CustomerRepository,
                   private val restaurantRepository: RestaurantRepository,
                   private val foodRepository: FoodRepository,
                   private val orderRepository: OrderRepository,
                   private val basketRepository: BasketRepository,
                   private val firebaseCloudMessageService: FirebaseCloudMessageService) {

    fun findOrder(orderId: Long) = orderRepository.findOne(orderId)

    fun findOrderWithRestWithOrderFoodsWithFood(orderId: Long) =
        orderRepository.findOneWithRestWithOrderFoodsWithFood(orderId)

    fun isAbleToAddToBasket(customerId: Long, restaurantId: Long): Boolean {
        val customer = customerRepository.findOne(customerId)

        // 직전에 장바구니에 저장한 음식의 음식점 PK
        val basketKey = customer.basketKey

        // 고객의 장바구니에 아무 것도 담겨있지 않거나 또는 같은 음식점의 음식을 담으려고 하는 경우 TRUE 반환
        return basketKey == null || basketKey == restaurantId
    }

    @Transactional
    fun addToBasket(customerId: Long, restaurantId: Long, basketDto: BasketRequestDto) {
        val customer = customerRepository.findOne(customerId)
        val food = foodRepository.findOne(basketDto.foodId)
        val basket = Basket.createBasket(customer, food, basketDto.count)

        // 장바구니에 저장
        basketRepository.save(basket)

        // basket_key 를 현재 장바구니에 저장한 음식의 음식점 PK로 UPDATE
        customer.changeBasketKey(restaurantId)
    }

    @Transactional
    fun removeToBasket(basketId: Long, customerId: Long) {
        basketRepository.remove(basketId)
        val customer = customerRepository.findOneWithBasket(customerId)
        val basketCount = customer.baskets.size

        // 장바구니에 담긴 음식이 없으면
        if (basketCount <= 0) {
            customer.changeBasketKey(null)
        }
    }

    @Transactional
    fun putFoodCntInBasket(customerId: Long, basketRequestDtos: List<BasketPutDto>) =
        basketRepository.putCount(customerId, basketRequestDtos)

    @Transactional
    fun removeAllToBasket(customerId: Long) {
        basketRepository.removeAll(customerId)
        val customer = customerRepository.findOne(customerId)
        customer.changeBasketKey(null)
    }

    @Transactional
    fun order(customerId: Long, orderDto: OrderDto): Long? {
        var totalPrice = 0 // 총 주문 금액
        val customer = customerRepository.findOneWithBasketWithFood(customerId)
        val orderFoods: MutableList<OrderFood> = ArrayList()
        val messageBuilder = StringBuilder(customer.baskets.size * 30)

        // 장바구에 담겨있는 음식들
        for (basket in customer.baskets) {
            totalPrice += basket.food.price * basket.count
            val orderFood = createOrderFood(basket.food, basket.food.price, basket.count)
            orderFoods.add(orderFood)
            // FCM message 추가
            messageBuilder.append(String.format("%s: %d개, ", basket.food.foodName, basket.count))
        }
        // FCM message 추가
        messageBuilder.append(String.format("총 주문 금액: %d원", totalPrice))
        val restaurant = restaurantRepository.findOneWithOwner(customer.basketKey!!)
        val token = restaurant.owner!!.firebaseToken!!

        // 주문 생성
        val order = createOrder(
            customer, restaurant, orderFoods, orderDto.orderType,
            orderDto.tableNumber, totalPrice, messageBuilder.toString()
        )
        // 고객 주문시간 등록
        order.registerReceivedTime()
        orderRepository.save(order)
        try {
            // 점주에게 FCM 발신
            firebaseCloudMessageService.sendMessageTo(
                token, "(주문접수) 주문이 접수되었습니다.",
                messageBuilder.toString(), orderDto.orderType
            )
        } catch (e: IOException) {
            throw Exception()
        }
        return order.id
    }

    @Transactional
    fun orderCancel(orderId: Long): Boolean {
        val order = orderRepository.findOneWithRestaurantWithOwner(orderId)
        if (order.isAbleToCancel) {
            order.cancel()
            //order.removeAllOrderFood();
            order.registerCanceledTime()
            val firebaseToken = order.restaurant.owner!!.firebaseToken!!
            val message = String.format(
                "[%s] 고객의 요청으로 접수된 주문이 취소되었습니다.",
                order.canceledOrCompletedTime!!.format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss"))
            )
            try {
                firebaseCloudMessageService.sendMessageTo(firebaseToken, "(주문취소) 주문 취소 알림!!", message, OrderType.CANCEL)
            } catch (e: IOException) {
                throw Exception()
            }
            return true
        }
        return false
    }

    @Transactional
    fun orderOwnerCancel(orderId: Long, messageDto: MessageDto): Boolean {
        val order = orderRepository.findOneWithCustomer(orderId)
        if (order.isAbleToOwnerCancel) {
            order.cancel()
            order.registerCanceledTime()
            try {
                // 고객이 회원탈퇴를 했을 경우 NPE 발생
                val firebaseToken = order.customer!!.firebaseToken!!
                val message = String.format(
                    "[%s] %s 사유로 인해 주문이 취소되었습니다.",
                    order.canceledOrCompletedTime!!.format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss")),
                    messageDto.message
                )
                firebaseCloudMessageService.sendMessageTo(
                    firebaseToken, "(주문취소) 매장의 요청으로 주문이 취소되었습니다.",
                    message, OrderType.CANCEL
                )
            } catch (e: IOException) {
                throw Exception()
            } catch (e: NullPointerException) {
                // 고객이 회원탈퇴를 했을 경우
            }
            return true
        }
        return false
    }

    // 주문 체크
    @Transactional
    fun orderCheck(orderId: Long): Boolean {
        val order = orderRepository.findOneWithCustomerAndRestaurant(orderId)
        if (order.isAbleToCheck) {
            order.check()
            order.registerCheckedTime()
            // 매장 주문 카운터 증가
            order.restaurant.increaseOrderCount()
            // 증가된 카운터 내 주문번호로 등록
            order.registerMyOrderNumber(order.restaurant.orderCount)
            try {
                // 고객이 회원탈퇴를 했을 경우 NPE 발생
                val firebaseToken = order.customer!!.firebaseToken!!
                val message = String.format(
                    "[%s] 조리가 시작되었습니다. 약 %d분 후 조리가 완료될 예정입니다.",
                    order.checkedTime!!.format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss")),
                    order.restaurant.orderingWaitingTime
                )
                firebaseCloudMessageService.sendMessageTo(
                    firebaseToken, "(접수완료) 요청하신 주문이 접수완료되었습니다.",
                    message, order.orderType
                )
            } catch (e: IOException) {
                throw Exception()
            } catch (e: NullPointerException) {
                // 고객이 회원탈퇴를 했을 경우
            }
            return true
        }
        return false
    }

    // 주문 완료
    @Transactional
    fun orderComplete(orderId: Long): Boolean {
        val order = orderRepository.findOneWithCustomer(orderId)
        if (order.isAbleToComplete) {
            order.complete()
            order.registerCompletedTime()
            try {
                // 고객이 회원탈퇴를 했을 경우 NPE 발생
                val firebaseToken = order.customer!!.firebaseToken!!
                val message = String.format(
                    "[%s] 최상의 맛을 느낄 수 있도록 지금 바로 %s",
                    order.canceledOrCompletedTime!!.format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss")),
                    if (order.orderType === OrderType.PACKING) "매장에서 픽업해주세요." else "픽업대에서 음식을 가져가주세요."
                )
                firebaseCloudMessageService.sendMessageTo(
                    firebaseToken, "(조리완료) 주문하신 음식이 조리완료 되었습니다.",
                    message, order.orderType
                )
            } catch (e: IOException) {
                throw Exception()
            } catch (e: NullPointerException) {
                // 고객이 회원탈퇴를 했을 경우
            }
            return true
        }
        return false
    }

    fun findBasketByCustomerIdAndFoodId(customerId: Long, foodId: Long): Basket? {
        return try {
            basketRepository.findOneByCustomerIdAndFoodId(customerId, foodId)
        } catch (e: NoResultException) {
            null
        }
    }

    @Transactional // 음식 수량만 증가(원래 수량 + 요청 수량)
    fun updateBasketCount(basket: Basket, basketRequestDto: BasketRequestDto) =
        basket.updateCount(basket.count + basketRequestDto.count)

    fun findOngoingOrders(restaurantId: Long) = orderRepository.findAllOrderedChecked(restaurantId)

    fun findFinishedOrders(restaurantId: Long) = orderRepository.findAllCanceledCompleted(restaurantId)

    companion object {
        private val log = LoggerFactory.getLogger(OrderService::class.java)
    }
}
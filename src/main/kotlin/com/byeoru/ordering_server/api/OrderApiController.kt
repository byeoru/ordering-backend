package com.byeoru.ordering_server.api

import com.byeoru.ordering_server.domain.Basket
import com.byeoru.ordering_server.domain.dto.ResultDto
import com.byeoru.ordering_server.domain.dto.request.BasketPutDto
import com.byeoru.ordering_server.domain.dto.request.BasketRequestDto
import com.byeoru.ordering_server.domain.dto.request.MessageDto
import com.byeoru.ordering_server.domain.dto.request.OrderDto
import com.byeoru.ordering_server.domain.dto.response.OrderPreviewDto
import com.byeoru.ordering_server.domain.dto.response.OrderPreviewWithRestSimpleDto
import com.byeoru.ordering_server.service.OrderService
import org.springframework.web.bind.annotation.*


@RestController
class OrderApiController(val orderService: OrderService) {

    /**
     * 장바구니에 추가
     */
    @PostMapping("/api/order/basket")
    fun addToBasket(
        @RequestParam(name = "customer_id") customerId: Long,
        @RequestParam(name = "restaurant_id") restaurantId: Long,
        @RequestBody dto: BasketRequestDto
    ): ResultDto<Boolean> {

        // 이미 저장되이 있는 음식인지 조회
        val basket: Basket? = orderService.findBasketByCustomerIdAndFoodId(customerId, dto.foodId)

        // 이미 저장된 음식일 경우
        basket?.let {
            orderService.updateBasketCount(basket, dto)
            return ResultDto(1, true)
        }

        // 장바구니에 추가 가능한지 체크
        val bAbleToAddToBasket: Boolean = orderService.isAbleToAddToBasket(customerId, restaurantId)
        return if (bAbleToAddToBasket) {
            orderService.addToBasket(customerId, restaurantId, dto)
            ResultDto(1, true)
        } else {
            ResultDto(1, false)
        }
    }

    /**
     * 장바구니 음식(여러개) 수량 변경
     */
    @PutMapping("/api/order/baskets")
    fun putFoodCntInBasket(
        @RequestParam(name = "customer_id") customerId: Long,
        @RequestBody basketPutDtos: List<BasketPutDto>
    ): ResultDto<Boolean> {
        orderService.putFoodCntInBasket(customerId, basketPutDtos)
        return ResultDto(1, true)
    }

    /**
     * 장바구니에서 음식 삭제
     */
    @DeleteMapping("/api/order/basket/{basketId}")
    fun removeToBasket(
        @PathVariable basketId: Long,
        @RequestParam(name = "customer_id") customerId: Long
    ): ResultDto<Boolean> {
        orderService.removeToBasket(basketId, customerId)
        return ResultDto(1, true)
    }

    /**
     * 장바구니 비우기
     */
    @DeleteMapping("/api/order/baskets")
    fun removeAllBaskets(@RequestParam(name = "customer_id") customerId: Long): ResultDto<Boolean> {
        orderService.removeAllToBasket(customerId)
        return ResultDto(1, true)
    }

    /**
     * 장바구니 음식 주문
     */
    @PostMapping("/api/order")
    fun order(
        @RequestParam(name = "customer_id") customerId: Long,
        @RequestBody dto: OrderDto
    ): ResultDto<Long> {
        // 주문
        val orderId: Long? = orderService.order(customerId, dto)
        // 장바구니 비우기
        orderService.removeAllToBasket(customerId)
        return ResultDto(1, orderId)
    }

    /**
     * 고객 주문 취소
     */
    @PostMapping("/api/order/{orderId}/cancel")
    fun cancel(@PathVariable orderId: Long): ResultDto<OrderPreviewWithRestSimpleDto> {
        val bCanceled: Boolean = orderService.orderCancel(orderId)
        return if (bCanceled) {
            val order = orderService.findOrder(orderId)
            val previewWithRestSimpleDto = OrderPreviewWithRestSimpleDto(order)
            ResultDto(1, previewWithRestSimpleDto)
        } else {
            ResultDto(1, null)
        }
    }

    /**
     * 점주 주문 취소
     */
    @PostMapping("/api/order/{orderId}/owner_cancel")
    fun ownerCancel(
        @PathVariable orderId: Long,
        @RequestBody dto: MessageDto
    ): ResultDto<OrderPreviewDto> {
        val bCanceled: Boolean = orderService.orderOwnerCancel(orderId, dto)
        return if (bCanceled) {
            val order = orderService.findOrder(orderId)
            val orderPreviewDto = OrderPreviewDto(order)
            ResultDto(1, orderPreviewDto)
        } else {
            ResultDto(1, null)
        }
    }

    /**
     * 주문 체크
     */
    @PostMapping("/api/order/{orderId}/check")
    fun check(@PathVariable orderId: Long): ResultDto<OrderPreviewDto> {
        val bChecked: Boolean = orderService.orderCheck(orderId)

        return if (bChecked) {
            val order = orderService.findOrder(orderId)
            val orderPreviewDto = OrderPreviewDto(order)
            ResultDto(1, orderPreviewDto)
        } else {
            ResultDto(1, null)
        }
    }

    /**
     * 주문 완료
     */
    @PostMapping("/api/order/{orderId}/complete")
    fun complete(@PathVariable orderId: Long): ResultDto<OrderPreviewDto> {
        val bCompleted: Boolean = orderService.orderComplete(orderId)

        return if (bCompleted) {
            val order = orderService.findOrder(orderId)
            val orderPreviewDto = OrderPreviewDto(order)
            ResultDto(1, orderPreviewDto)
        } else {
            ResultDto(1, null)
        }
    }
}
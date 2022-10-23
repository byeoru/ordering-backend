package com.byeoru.ordering_server.api

import com.byeoru.ordering_server.domain.dto.ResultDto
import com.byeoru.ordering_server.domain.dto.request.WaitingRegisterDto
import com.byeoru.ordering_server.service.CustomerService
import com.byeoru.ordering_server.service.WaitingService
import org.springframework.web.bind.annotation.*

@RestController
class WaitingApiController(private val waitingService: WaitingService,
                           private val customerService: CustomerService) {

    /**
     * 웨이팅 접수
     */
    @PostMapping("/api/waiting")
    fun registerWaiting(@RequestParam(name = "restaurant_id") restaurantId: Long,
                        @RequestParam(name = "customer_id") customerId: Long,
                        @RequestBody dto: WaitingRegisterDto): ResultDto<Boolean> {
        val customer = customerService.findCustomerWithPhoneNumberWithWaiting(customerId)
        // 웨이팅 접수가 가능한지 체크
        return if (customer.isAbleToWaiting()) {
            waitingService.registerWaiting(restaurantId, customerId, dto)
            ResultDto(1, true)
        } else {
            ResultDto(1, false)
        }
    }

    /**
     * 웨이팅 취소
     */
    @DeleteMapping("/api/waiting/{waitingId}")
    fun cancelWaiting(@PathVariable waitingId: Long): ResultDto<Boolean> {
        waitingService.cancelOrDeleteWaiting(waitingId)
        return ResultDto(1, true)
    }
}
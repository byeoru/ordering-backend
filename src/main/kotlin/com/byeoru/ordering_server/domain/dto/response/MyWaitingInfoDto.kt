package com.byeoru.ordering_server.domain.dto.response

import com.byeoru.ordering_server.domain.Waiting
import java.time.format.DateTimeFormatter

class MyWaitingInfoDto(waiting: Waiting, numberInFrontOfMe: Long) {

    val waitingId: Long?
    val myWaitingNumber: Int
    val numInFrontOfMe: Long
    val estimatedWaitingTime : Int
    val waitingRegisterTime: String?
    val restaurantId: Long?
    val restaurantName: String
    val profileImageUrl: String?
    val backgroundImageUrl: String?

    init {
        this.waitingId = waiting.id
        this.myWaitingNumber = waiting.myWaitingNumber
        this.numInFrontOfMe = numberInFrontOfMe
        this.estimatedWaitingTime = waiting.restaurant.admissionWaitingTime * (numberInFrontOfMe.toInt() + 1)
        this.waitingRegisterTime =
            waiting.waitingRegisterTime?.format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss"))
        this.restaurantId = waiting.restaurant.id
        this.restaurantName = waiting.restaurant.restaurantName
        this.profileImageUrl = waiting.restaurant.profileImageUrl
        this.backgroundImageUrl = waiting.restaurant.backgroundImageUrl
    }
}
package com.byeoru.ordering_server.domain.dto.response

import com.byeoru.ordering_server.domain.Waiting
import java.time.format.DateTimeFormatter

class WaitingPreviewDto(waiting: Waiting) {

    val waitingId: Long?
    val waitingNumber: Int
    val numOfTeamMembers: Byte
    val phoneNumber: String
    val waitingRegisterTime: String?

    init {
        waitingId = waiting.id
        waitingNumber = waiting.myWaitingNumber
        numOfTeamMembers = waiting.numOfTeamMembers
        phoneNumber = waiting.phoneNumber.phoneNumber
        waitingRegisterTime =
            waiting.waitingRegisterTime?.format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss"))
    }
}
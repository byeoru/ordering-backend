package com.byeoru.ordering_server.domain.dto.response

data class QrcodeResultDto(val waitingUrl: String,
                           val togoUrl: String,
                           val tableUrls: List<String>)
package com.byeoru.ordering_server.domain.dto.request

data class BasketRequestDto(val foodId: Long,
                            val count: Int)
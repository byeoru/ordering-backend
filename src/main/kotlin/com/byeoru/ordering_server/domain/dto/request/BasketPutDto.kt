package com.byeoru.ordering_server.domain.dto.request

data class BasketPutDto(val basketId: Long,
                        val count: Int)
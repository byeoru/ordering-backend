package com.byeoru.ordering_server.domain.dto.response

data class CustomerSignInResultDto(val customerId: Long,
                                   val nickname: String,
                                   val basketCount: Int)
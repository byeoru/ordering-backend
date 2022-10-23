package com.byeoru.ordering_server.domain.dto.request

data class OwnerSignUpDto(val signInId: String,
                          val password: String,
                          val phoneNumber: String)
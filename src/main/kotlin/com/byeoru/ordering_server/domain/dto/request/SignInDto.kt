package com.byeoru.ordering_server.domain.dto.request

data class SignInDto(val signInId: String,
                     val password: String,
                     val firebaseToken: String)
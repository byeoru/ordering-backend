package com.byeoru.ordering_server.domain.dto.request

data class PasswordChangeDto(val currentPassword: String,
                             val newPassword: String)
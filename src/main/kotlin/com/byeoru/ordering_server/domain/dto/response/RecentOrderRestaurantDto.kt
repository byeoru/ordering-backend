package com.byeoru.ordering_server.domain.dto.response

data class RecentOrderRestaurantDto(val restaurantId: Long,
                                    val restaurantName: String,
                                    val profileImageUrl: String,
                                    val backgroundImageUrl: String,
                                    val rating: Float,
                                    val orderingWaitingTime: Int)
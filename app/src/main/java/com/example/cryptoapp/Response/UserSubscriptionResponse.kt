package com.example.cryptoapp.Response

data class UserSubscriptionResponse(
    val code: Int,
    val `data`: List<UserSubscriptionDataResponse>,
    val isSuccess: Boolean,
    val message: Any
)
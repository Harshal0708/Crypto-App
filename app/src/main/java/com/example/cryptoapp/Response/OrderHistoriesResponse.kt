package com.example.cryptoapp.Response

data class OrderHistoriesResponse(
    val code: Int,
    val `data`: Data,
    val isSuccess: Boolean,
    val message: String
)
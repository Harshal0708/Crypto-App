package com.example.cryptoapp.Response

data class OrderHistoriesResponse(
    val code: Int,
    val `data`: DataXXX,
    val isSuccess: Boolean,
    val message: String
)
package com.example.cryptoapp.Response

data class OrderHistoriesResponse(
    val code: Int,
    val `data`: OrderHistoriesdataResponse,
    val isSuccess: Boolean,
    val message: String
)
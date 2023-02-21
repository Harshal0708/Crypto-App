package com.example.cryptoapp.Response

data class ForgotResponse(
    val code: Int,
    val forgotDataResponse: ForgotDataResponse,
    val isSuccess: Boolean,
    val message: String
)
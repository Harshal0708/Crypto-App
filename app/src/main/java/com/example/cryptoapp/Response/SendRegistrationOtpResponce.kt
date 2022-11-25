package com.example.cryptoapp.Response

data class SendRegistrationOtpResponce(
    val code: Int,
    val isSuccess: Boolean,
    val message: String
)
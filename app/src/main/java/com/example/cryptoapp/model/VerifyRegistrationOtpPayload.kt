package com.example.cryptoapp.model

data class VerifyRegistrationOtpPayload(
    val email: String,
    val emailOtp: String,
    val mobile: String,
    val mobileOtp: String
)
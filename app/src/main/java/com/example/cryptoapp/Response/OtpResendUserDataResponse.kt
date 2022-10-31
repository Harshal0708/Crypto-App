package com.example.cryptoapp.Response

data class OtpResendUserDataResponse(
    val email : String,
    val mobile : String,
    val mobile_OTP : String,
    val email_OTP : String,
)


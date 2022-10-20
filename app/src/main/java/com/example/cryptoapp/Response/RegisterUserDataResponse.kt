package com.example.cryptoapp.Response

data class RegisterUserDataResponse(
    val userID : String,
    val token : String,
    val mobile_OTP : String,
    val email_OTP : String,
    val email : String,
    val mobile_No : String,
)


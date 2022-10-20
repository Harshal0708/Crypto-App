package com.example.cryptoapp.Response

data class LoginUserDataResponse(
    val userId : String,
    val name : String,
    val email : String,
    val password : String,
    val mobile : String,
    val refreshTokenExpiryTime : String,
    val refreshToken : String,
    val accessToken : String,
    val rememberMe : String,
    val otp : String,
)


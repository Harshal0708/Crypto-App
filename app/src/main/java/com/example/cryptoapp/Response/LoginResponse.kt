package com.example.cryptoapp.Response

data class LoginResponse(
    val code : String,
    val message : String,
    val data : LoginUserDataResponse,
)
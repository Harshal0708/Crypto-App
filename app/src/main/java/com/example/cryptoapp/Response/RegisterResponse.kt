package com.example.cryptoapp.Response

data class RegisterResponse(
    val code : String,
    val message : String,
    val data : RegisterUserDataResponse,
)


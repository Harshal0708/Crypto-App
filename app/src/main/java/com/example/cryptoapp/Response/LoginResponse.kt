package com.example.cryptoapp.Response

data class LoginResponse(
    val code: Int,
    val `data`: DataXX,
    val isSuccess: Boolean,
    val message: String
)
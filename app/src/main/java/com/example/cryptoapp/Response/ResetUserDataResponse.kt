package com.example.cryptoapp.Response

data class ResetUserDataResponse(
    val email : String,
    val password : String,
    val confirmPassword : String,
    val code : String,
)


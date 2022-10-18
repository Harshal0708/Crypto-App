package com.example.cryptoapp.Response

data class ForgotResponse(
    val code : String,
    val message : String,
    val data : ForgotUserDataResponse,
)


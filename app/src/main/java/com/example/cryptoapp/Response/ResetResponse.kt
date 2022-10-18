package com.example.cryptoapp.Response

data class ResetResponse(
    val code : String,
    val message : String,
    val data : ResetUserDataResponse,
)


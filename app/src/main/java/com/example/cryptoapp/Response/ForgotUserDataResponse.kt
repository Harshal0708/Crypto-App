package com.example.cryptoapp.Response

data class ForgotUserDataResponse(
    val email : String,
    val code : String,
    val new_Password : String
)


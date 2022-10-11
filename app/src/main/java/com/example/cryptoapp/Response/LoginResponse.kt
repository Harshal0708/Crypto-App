package com.example.cryptoapp.Response

data class LoginResponse(
    val resultCode : String,
    val resultMessage : String,
    val jwtToken : String,
    val moduleAccess : String
)
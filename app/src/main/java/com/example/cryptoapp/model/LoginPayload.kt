package com.example.cryptoapp.model

class LoginPayload(
    var userId: String,
    var name: String,
    var email: String,
    var password: String,
    var mobile: String,
    var refreshTokenExpiryTime: String,
    var refreshToken: String,
    var accessToken: String,
    var rememberMe: Boolean,
    var otp: String,
)

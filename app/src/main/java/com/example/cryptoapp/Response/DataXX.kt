package com.example.cryptoapp.Response

data class DataXX(
    val accessToken: String,
    val countryCode: Int,
    val countryId: String,
    val email: String,
    val firstName: String,
    val haveAnySubscription: Boolean,
    val isKycVerify: Boolean,
    val lastName: String,
    val mobile: String,
    val name: String,
    val profilePicture: String,
    val refreshToken: String,
    val refreshTokenExpiryTime: String,
    val userId: String
)
package com.example.cryptoapp.model

data class RegisterPayload(
    val adharCardNumber: String,
    val apiKey: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val panCardNumber: String,
    val password: String,
    val phoneNumber: String,
    val profileImage: String,
    val secreteKey: String
)
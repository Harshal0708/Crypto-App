package com.example.cryptoapp.Response

data class UserDetailsResponse(
    val apiKey: Any,
    val email: String,
    val firstName: String,
    val id: String,
    val lastName: String,
    val phoneNumber: String,
    val profileImage: String,
    val profilePicture: String,
    val secretKey: Any
)
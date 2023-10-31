package com.example.cryptoapp.model

data class CreateUserSubscriptionPayload(
    val planId: String,
    val subId: String,
    val userId: String
)
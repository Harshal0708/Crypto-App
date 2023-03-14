package com.example.cryptoapp.model

data class CreateApiKeysPayload(
    val apiKey: String,
    val secreteKey: String,
    val userId: String
)
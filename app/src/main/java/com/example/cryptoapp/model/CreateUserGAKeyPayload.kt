package com.strings.cryptoapp.model

data class CreateUserGAKeyPayload(
    val gaKey: String,
    val id: String,
    val userId: String
)
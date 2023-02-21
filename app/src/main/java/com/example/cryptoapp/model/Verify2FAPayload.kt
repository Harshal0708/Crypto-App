package com.strings.cryptoapp.model

data class Verify2FAPayload(
    val otp: String,
    val secretKey: String
)
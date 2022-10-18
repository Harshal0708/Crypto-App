package com.example.cryptoapp.model

class ResetPayload(
    var email: String,
    var password: String,
    var confirmPassword: String,
    var code: String,
)

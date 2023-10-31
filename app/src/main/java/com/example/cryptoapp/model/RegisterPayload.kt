package com.example.cryptoapp.model

import java.io.File

data class RegisterPayload(
    val FirstName: String,
    val LastName: String,
    val Password: String,
    val Email: String,
    val CountryId: String,
    val PhoneNumber: String,
    val ProfileImage: File,
    val ImageURL: String,

)
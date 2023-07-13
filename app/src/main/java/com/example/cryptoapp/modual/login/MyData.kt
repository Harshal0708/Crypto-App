package com.example.cryptoapp.modual.login

import android.graphics.Bitmap

data class MyData(
    val email: String,
    val phone: String,
    val firsName: String,
    val lastName: String,
    val rePassword: String,
    val imageUri: Bitmap,
    val countryId: String,
)

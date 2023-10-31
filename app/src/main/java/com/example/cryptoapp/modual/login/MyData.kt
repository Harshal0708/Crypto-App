package com.example.cryptoapp.modual.login

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

data class MyData(
    val email: String,
    val phone: String,
    val firsName: String,
    val lastName: String,
    val rePassword: String,
    val imageUri: String,
    val countryId: String,
    val countryCode: Int
)

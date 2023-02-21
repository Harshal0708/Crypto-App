package com.strings.cryptoapp.Response

data class GenerateQrCodeResponnse(
    val code: Int,
    val `data`: Any,
    val isSuccess: Boolean,
    val message: Any
)
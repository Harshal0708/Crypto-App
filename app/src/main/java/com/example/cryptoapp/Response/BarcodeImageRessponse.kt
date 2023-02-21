package com.strings.cryptoapp.Response

data class BarcodeImageResponse(
    val code: Int,
    val data: Data,
    val isSuccess: Boolean,
    val message: Any
)
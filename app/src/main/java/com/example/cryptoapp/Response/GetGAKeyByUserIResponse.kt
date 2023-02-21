package com.strings.cryptoapp.Response

data class GetGAKeyByUserIResponse(
    val code: Int,
    val `data`: String,
    val isSuccess: Boolean,
    val message: Any
)
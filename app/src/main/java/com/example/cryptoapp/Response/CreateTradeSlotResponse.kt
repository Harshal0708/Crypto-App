package com.example.cryptoapp.Response

data class CreateTradeSlotResponse(
    val code: Int,
    val `data`: Any,
    val isSuccess: Boolean,
    val message: Any
)
package com.example.cryptoapp.Response

data class TradeSlotApiResponse(
    val code: Int,
    val `data`: List<DataXXXX>,
    val isSuccess: Boolean,
    val message: Any
)
package com.example.cryptoapp.Response

data class LiveOrderResponse(
    val TotalPL: Double,
    val liveOrderResponseModels: List<LiveOrderResponseModel>
)
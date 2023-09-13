package com.example.cryptoapp.Response

data class getLiveTopGainersResponseItem(
    val price: Double,
    val priceChangePercent: Double,
    val symbol: String
)
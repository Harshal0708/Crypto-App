package com.example.cryptoapp.Response

data class getLiveTopGainersResponseItem(
    val closeTime: String,
    val firstTradeId: Long,
    val lastQuantity: Double,
    val lastTradeId: Long,
    val openTime: String,
    val priceChange: Double,
    val priceChangePercent: Double,
    val totalTrades: Int,
    val weightedAveragePrice: Double,
    val symbol: String,
)
package com.example.cryptoapp.Response

data class OrderHistoryDetailResponseItem(
    val buyPrice: Double,
    val orderMode: Int,
    val pl: Double,
    val quantity: Double,
    val sellPrice: Double,
    val strategyId: String,
    val strategyNumber: Int,
    val symbol: String,
    val tradeEntryTime: String,
    val tradeExitTime: String,
    val tradingType: Int
)
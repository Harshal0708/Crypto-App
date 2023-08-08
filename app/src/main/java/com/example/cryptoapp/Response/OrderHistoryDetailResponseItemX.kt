package com.example.cryptoapp.Response

data class OrderHistoryDetailResponseItemX(
    val buyPrice: Double,
    val orderMode: Int,
    val pl: Double,
    val quantity: Double,
    val sellPrice: Double,
    val strategy: StrategyXXXX,
    val strategyId: String,
    val symbol: String,
    val tradeEntryTime: String,
    val tradeExitTime: String,
    val tradingType: Int
)
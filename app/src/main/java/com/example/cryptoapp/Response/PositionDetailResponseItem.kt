package com.example.cryptoapp.Response

data class PositionDetailResponseItem(
    val BuyPrice: Double,
    val OrderId: String,
    val OrderMode: Int,
    val PL: Double,
    val Quantity: Double,
    val SL: Double,
    val Strategy: StrategyXXX,
    val StrategyId: String,
    val StrategyNumber: Int,
    val Symbol: String,
    val Target: Double,
    val TradeDateTime: String,
    val TradingType: Int
)
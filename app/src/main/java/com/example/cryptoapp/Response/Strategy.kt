package com.example.cryptoapp.Response

data class Strategy(
    val CreatedDate: String,
    val Description: String,
    val Id: String,
    val IsActive: Boolean,
    val MinCapital: Double,
    val ModifiedDate: String,
    val MonthlyFee: Double,
    val StrategyName: String
)
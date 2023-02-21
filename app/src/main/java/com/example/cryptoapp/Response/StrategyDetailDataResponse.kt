package com.example.cryptoapp.Response

data class StrategyDetailDataResponse(
    val createdDate: String,
    val description: String,
    val id: String,
    val isActive: Boolean,
    val minCapital: Int,
    val modifiedDate: String,
    val monthlyFee: Int,
    val strategyName: String
)
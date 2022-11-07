package com.example.cryptoapp.Response

data class StrategyDetailRes(
    val createdDate: String,
    val description: String,
    val id: Int,
    val isActive: Boolean,
    val minCapital: Int,
    val modifiedDate: String,
    val monthlyFee: Int,
    val strategyName: String
)
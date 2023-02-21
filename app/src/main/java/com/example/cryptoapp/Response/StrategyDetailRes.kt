package com.example.cryptoapp.Response

data class StrategyDetailRes(
    val code: Int,
    val `data`: StrategyDetailDataResponse,
    val isSuccess: Boolean,
    val message: Any
)
package com.example.cryptoapp.Response

data class StrategyRes(
    val code: Int,
    val `data`: List<StrategyDataRes>,
    val isSuccess: Boolean,
    val message: Any
)
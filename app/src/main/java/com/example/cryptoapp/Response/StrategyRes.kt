package com.example.cryptoapp.Response

data class StrategyRes(
    val code: Int,
    val `data`: List<DataXXXX>,
    val isSuccess: Boolean,
    val message: Any
)
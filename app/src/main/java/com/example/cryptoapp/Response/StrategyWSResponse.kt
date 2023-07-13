package com.example.cryptoapp.Response

data class StrategyWSResponse(
    val StrategyPLVMs: List<StrategyPLVM>,
    val TotalPL: Double
)
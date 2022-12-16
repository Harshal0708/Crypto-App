package com.example.cryptoapp.Response

data class OrderHistory(
    val applicationUser: Any,
    val id: String,
    val price: Double,
    val quantity: Double,
    val quantityFilled: Double,
    val side: Int,
    val spotPrice: Double,
    val status: Int,
    val symbol: String,
    val timestamp: String,
    val type: Int,
    val userId: String
)
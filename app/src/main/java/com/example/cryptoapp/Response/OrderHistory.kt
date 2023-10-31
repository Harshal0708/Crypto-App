package com.example.cryptoapp.Response

data class OrderHistory(
    val applicationUser: Any,
    val id: String,
    val price: Double,
    val quantity: Double,
    val quantityFilled: Any,
    val side: Int,
    val slotId: String,
    val spotPrice: Double,
    val status: Int,
    val symbol: String,
    val timestamp: String,
    val tradeSlot: Any,
    val tradingType: Int,
    val type: Int,
    val userId: String
)
package com.example.cryptoapp.model

data class CreateTradeSlotPayload(
    val coins: String,
    val createdDate: String,
    val modifiedDate: String,
    val price: Int,
    val slotId: String,
    val strategyId: String,
    val tradingType: Int,
    val userId: String
)
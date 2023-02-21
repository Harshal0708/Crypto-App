package com.example.cryptoapp.model

data class GetOrderHistoryListPayload(
    val pageNumber: Int,
    val pageSize: Int,
    val userId: String
)
package com.example.cryptoapp.Response

data class UserSubscriptionDetail(
    val isActive: Boolean,
    val noOfStrategies: Int,
    val subscriptionId: String,
    val subscriptionName: String,
    val subscriptionPrice: Int
)
package com.example.cryptoapp.Response

data class UserSubscriptionItem(
    val isActive: Boolean,
    val subscriptionId: String,
    val subscriptionName: String,
    val subscriptionPrice: Int
)
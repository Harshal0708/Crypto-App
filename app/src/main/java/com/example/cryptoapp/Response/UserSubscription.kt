package com.example.cryptoapp.Response

data class UserSubscription(
    val applicationUser: Any,
    val id: String,
    val planId: String,
    val plans: Any,
    val subId: String,
    val subscription: Any,
    val subscriptionDate: String,
    val subscriptionExpDate: String,
    val subscriptionModifiedDate: String,
    val subscriptionStatus: Int,
    val userId: String
)
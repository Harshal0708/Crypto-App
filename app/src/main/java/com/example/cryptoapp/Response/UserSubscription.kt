package com.example.cryptoapp.Response

data class UserSubscription(
    val applicationUser: String,
    val id: String,
    val planId: String,
    val plans: Any,
    val subId: String,
    val subscription: String,
    val subscriptionDate: String,
    val subscriptionExpDate: String,
    val subscriptionModifiedDate: String,
    val subscriptionStatus: Int,
    val userId: String
)
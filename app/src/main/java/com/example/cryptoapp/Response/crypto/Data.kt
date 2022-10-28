package com.example.cryptoapp.Response.crypto

data class Data(
    val cryptoCurrencyList: List<CryptoCurrency>,
    val totalCount: String
)
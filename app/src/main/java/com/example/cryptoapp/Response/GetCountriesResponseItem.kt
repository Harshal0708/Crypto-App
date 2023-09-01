package com.example.cryptoapp.Response

data class GetCountriesResponseItem(
    val countryCode: Int,
    val countryName: String,
    val countryPrefix: String,
    val countryFlagURL: String,
    val id: String
)
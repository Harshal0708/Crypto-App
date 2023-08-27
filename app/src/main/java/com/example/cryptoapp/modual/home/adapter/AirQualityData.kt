package com.example.cryptoapp.modual.home.adapter

data class AirQualityData(
    var name: String,
    var price: String,
    var prePrice: String,
) {
    constructor() : this("", "","")
}
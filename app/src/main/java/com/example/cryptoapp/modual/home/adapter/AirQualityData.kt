package com.example.cryptoapp.modual.home.adapter

data class AirQualityData(
    var name: String,
    var price: String
) {

    val previousPrice = 0.0
    val priceChangeColor: Int
        get() =
            if (price >= 0.toString()) android.graphics.Color.GREEN else android.graphics.Color.RED


//            when {
//                price.toDouble() > previousPrice -> android.graphics.Color.GREEN
//                price.toDouble() < previousPrice -> android.graphics.Color.RED
//                else -> android.graphics.Color.GRAY
//            }
}
package com.example.cryptoapp.singleton

import com.example.cryptoapp.modual.home.adapter.AirQualityData
import javax.inject.Singleton

class MySingleton {

    companion object {
        private var pos = 0
        private var storedValue = ""
        var tickerResponseItem: AirQualityData = AirQualityData()
        private val instance = Singleton()

        fun getInstance(): Singleton {
            return instance
        }
    }

    fun setStoredValue(value: String) {
        storedValue = value
    }

    fun getStoredValue(): String {
        return storedValue
    }


    fun setTickerResponseItem(value: AirQualityData) {
        tickerResponseItem = value
    }

    fun getTickerResponseItem(): AirQualityData {
        return tickerResponseItem
    }

    fun setPos(value: Int) {
        pos = value
    }

    fun getPos(): Int {
        return pos
    }

}
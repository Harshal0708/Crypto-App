package com.example.cryptoapp.Response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TickerResponse : ArrayList<TickerResponseItem>()
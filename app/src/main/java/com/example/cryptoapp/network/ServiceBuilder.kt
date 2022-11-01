package com.example.cryptoapp.network

import com.example.cryptoapp.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL) // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val retrofit2 = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL2) // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create())

        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
    fun<T> buildServiceTwo(service: Class<T>): T{
        return retrofit2.create(service)
    }


}
package com.example.cryptoapp.network

import com.example.cryptoapp.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    //Create Logger
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(logger).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL) // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()


    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }


}
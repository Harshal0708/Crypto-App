package com.example.cryptoapp.network

import com.example.cryptoapp.Constants
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {


    //Create Logger

    private val logger = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)


    private val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .addInterceptor(OAuthInterceptor("Bearer", Constants.TOKEN))
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL) // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()


    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}

class OAuthInterceptor(
    private val tokenType: String,
    private val accessToken: String
) :

    Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", "$tokenType $accessToken").build()
        //  request = request.newBuilder().addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9lbWFpbGFkZHJlc3MiOiJhcHVydmEuc2t5dHR1c0BnbWFpbC5jb20iLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJDdXN0b21lciIsImp0aSI6ImY3NWEwNDNjLTQ1ODYtNDU3OS04NzIxLTE3MDYwYTY5ZmYzYyIsImV4cCI6MTY3MDQ5NzkxNSwiaXNzIjoiaHR0cDovLzEwMy4xNC45OS42MS8iLCJhdWQiOiJVc2VyIn0.--y_1Urvg5cOL5w7C9gpaEUlO9DtVWPme5K3VSLJnJw").build()

        return chain.proceed(request)
    }
}
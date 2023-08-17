package com.example.cryptoapp.network

import android.content.Context
import com.example.cryptoapp.Constants
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.modual.login.LoginActivity
import com.example.cryptoapp.preferences.MyPreferences
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceBuilder(context5: Context) {

    //Create Logger

    private val logger = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .addInterceptor(OAuthInterceptor("Bearer", context5))
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        //.baseUrl(Constants.LOCAl_URL) // change this IP for testing by your actual machine IP
        .baseUrl(Constants.STRIPE_PAYMENT_URL) // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}

class OAuthInterceptor(
    private val tokenType: String,
    private val context: Context
) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        var tok = ""
        if (tok != null) {
            tok = MyPreferences(context).getToken()
        }

        //For Api
        //request = request.newBuilder().header("Authorization", "$tokenType $tok").build()

        //For Stripe
         request = request.newBuilder().header("Authorization", "$tokenType sk_test_51MFAOfSHmxsQH4CHc0B63ccrQu8tu1m9ynAXYaEydRrSQwp4nhBqKtJFaEYZn9aTYhsdw1Ti8VHA9Cw4ZRcZR8Lg00qUCjkGZk").build()
        return chain.proceed(request)
    }
}
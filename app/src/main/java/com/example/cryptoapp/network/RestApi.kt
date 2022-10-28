package com.example.cryptoapp.network

import com.example.cryptoapp.Response.*
import com.example.cryptoapp.Response.crypto.CryptoRespo
import com.example.cryptoapp.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RestApi {
    @POST("login")
    fun addLogin(@Body loginPayload: LoginPayload): Call<LoginResponse>

    @POST("register")
    fun addRegister(@Body registerPayload: RegisterPayload): Call<RegisterResponse>

    @POST("forgotpassword")
    fun addForgotPassword(@Body forgotPayload: ForgotPayload): Call<ForgotResponse>


    @POST("resetpassword")
    fun addResetpassword(@Body resetPayload: ResetPayload): Call<ResetResponse>


    @POST("registrationconfirm")
    fun addOtp(
        @Query("emailOtp") emailOtp: String,
        @Query("mobileOtp") mobileOtp: String,
        @Query("email") email: String,
        @Query("mobile") mobile: String,
    ): Call<OtpResponse>


    @POST("resendotp")
    fun addResendOtp(
        @Query("email") email: String,
        @Query("mobile") mobile: String,
    ): Call<OtpResendResponse>


    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=10")
   suspend fun getMarketDate(): Response<CryptoRespo>
   // fun getMarketDate(): Call<CryptoRespo>
}
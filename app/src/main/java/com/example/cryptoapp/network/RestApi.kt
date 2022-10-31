package com.example.cryptoapp.network

import com.example.cryptoapp.Response.*
import com.example.cryptoapp.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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


}
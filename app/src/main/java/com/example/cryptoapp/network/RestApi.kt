package com.example.cryptoapp.network

import com.example.cryptoapp.Response.ForgotResponse
import com.example.cryptoapp.Response.LoginResponse
import com.example.cryptoapp.Response.RegisterResponse
import com.example.cryptoapp.Response.ResetResponse
import com.example.cryptoapp.model.ForgotPayload
import com.example.cryptoapp.model.LoginPayload
import com.example.cryptoapp.model.RegisterPayload
import com.example.cryptoapp.model.ResetPayload
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RestApi {
    @POST("LogIn")
    fun addLogin(@Body loginPayload: LoginPayload): Call<LoginResponse>

    @POST("register")
    fun addRegister(@Body registerPayload: RegisterPayload): Call<RegisterResponse>

    @POST("forgotpassword")
    fun addForgotPassword(@Body forgotPayload: ForgotPayload): Call<ForgotResponse>


    @POST("resetpassword")
    fun addResetpassword(@Body resetPayload: ResetPayload): Call<ResetResponse>

}
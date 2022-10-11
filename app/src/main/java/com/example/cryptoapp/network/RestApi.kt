package com.example.cryptoapp.network

import com.example.cryptoapp.Response.LoginResponse
import com.example.cryptoapp.model.LoginPayload
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RestApi {
    @POST("LogIn")
    fun addLogin(@Body loginPayload: LoginPayload): Call<LoginResponse>
}
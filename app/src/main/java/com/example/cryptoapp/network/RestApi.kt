package com.example.cryptoapp.network

import com.example.cryptoapp.Constants
import com.example.cryptoapp.Response.*
import com.example.cryptoapp.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApi {
    @POST(Constants.login)
    fun addLogin(@Body loginPayload: LoginPayload): Call<LoginResponse>

    @POST(Constants.register)
    fun addRegister(@Body registerPayload: RegisterPayload): Call<RegisterResponse>

    @POST(Constants.forgotpassword)
    fun addForgotPassword(@Body forgotPayload: ForgotPayload): Call<ForgotResponse>


    @POST(Constants.resetpassword)
    fun addResetpassword(@Body resetPayload: ResetPayload): Call<ResetResponse>


    @POST(Constants.registrationconfirm)
    fun addOtp(
        @Query("emailOtp") emailOtp: String,
        @Query("mobileOtp") mobileOtp: String,
        @Query("email") email: String,
        @Query("mobile") mobile: String,
    ): Call<OtpResponse>


    @POST(Constants.resendotp)
    fun addResendOtp(
        @Query("email") email: String,
        @Query("mobile") mobile: String,
    ): Call<OtpResendResponse>

    @GET(Constants.strategy)
    suspend fun getStrategy(): Response<StrategyRes>

    @GET(Constants.userdetails)
    suspend fun getUserDetails(@Query("userId") userId: String): Response<UserDetailsResponse>

    @GET("${Constants.strategy}/{id}")
    suspend fun getStrategyById(@Path("id") id: Int): Response<StrategyDetailRes>

    @GET(Constants.getplans)
    suspend fun getPlans(): Response<GetPlanResponse>

    @POST(Constants.getusersubscription)
    fun addUserSubscription(@Body userSubscriptionModel: UserSubscriptionModel): Call<UserSubscriptionResponse>

    @POST(Constants.getSubscriptionDetails)
    fun addSubscriptionDetails(@Body userSubscriptionModel: UserSubscriptionModel): Call<UserSubscriptionDetail>



}
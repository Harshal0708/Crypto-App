package com.example.cryptoapp.network

import com.example.cryptoapp.Constants
import com.example.cryptoapp.Response.*
import com.example.cryptoapp.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Objects

interface RestApi {
    @POST(Constants.login)
    fun addLogin(@Body loginPayload: LoginPayload): Call<LoginResponse>

    @POST(Constants.verifyLoginOtp)
    fun addVerifyLoginOtp(@Body verifyLoginOtpPayload: VerifyLoginOtpPayload): Call<SendRegistrationOtpResponce>

    @POST(Constants.sendLoginOtp)
    fun addSendLoginOtp(@Body sendLoginOtpPayload: SendLoginOtpPayload): Call<SendRegistrationOtpResponce>


    @FormUrlEncoded
    @POST(Constants.register)
    fun addRegister(
        @Field("FirstName") FirstName: String,
        @Field("LastName") LastName: String,
        @Field("Password") Password: String,
        @Field("Email") Email: String,
        @Field("ApiKey") ApiKey: String,
        @Field("SecreteKey") SecreteKey: String,
        @Field("AdharCardNumber") AdharCardNumber: String,
        @Field("PanCardNumber") PanCardNumber: String,
        @Field("PhoneNumber") PhoneNumber: String,
        @Field("ProfileImage") ProfileImage: String,
        @Field("ProfilePicture") ProfilePicture: String,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST(Constants.updateProfileDetail)
    fun updateProfileDetail(
        @Field("id") id: String,
        @Field("email") email: String,
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("profileImage") profileImage: String,
        @Field("phoneNumber") phoneNumber: String,
        @Field("apiKey") apiKey: String,
        @Field("secretKey") secretKey: String,
    ): Call<Userupdatedsuccessfully>

    @POST(Constants.sendRegistrationOtp)
    fun addSendRegistrationOtp(@Body sendRegistrationOtpPayload: SendRegistrationOtpPayload): Call<SendRegistrationOtpResponce>


    @POST(Constants.verifyRegistrationOtp)
    fun addVerifyRegistrationOtpp(@Body verifyRegistrationOtpPayload: VerifyRegistrationOtpPayload): Call<SendRegistrationOtpResponce>


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

    @GET(Constants.strategy)
    suspend fun getStrategy(): Response<StrategyRes>


    @GET(Constants.getProfileDetail)
    suspend fun getUserDetails(@Query("email") email: String): Response<UserDetailsResponse>

    @GET("${Constants.strategy}/{id}")
    suspend fun getStrategyById(@Path("id") id: Int): Response<StrategyDetailRes>

    @GET(Constants.getplans)
    suspend fun getPlans(): Response<GetPlanResponse>

    @POST(Constants.getusersubscription)
    fun addUserSubscription(@Body userSubscriptionModel: UserSubscriptionModel): Call<UserSubscriptionResponse>

    @POST(Constants.getSubscriptionDetails)
    fun addSubscriptionDetails(@Body userSubscriptionModel: UserSubscriptionModel): Call<UserSubscriptionDetail>


}
package com.example.cryptoapp.network

import com.example.cryptoapp.Constants
import com.example.cryptoapp.Response.*
import com.example.cryptoapp.model.*
import com.example.cryptoapp.modual.card.CardPaymentIntentResponse
import com.example.cryptoapp.modual.payment.CreateCustomerIdResponse
import com.example.cryptoapp.modual.payment.EphemeralKeyResponse
import com.example.cryptoapp.modual.payment.PaymentIntentRespomse
import com.example.cryptoapp.modual.payment.StripeConfirmationResponse
import com.example.cryptoapp.modual.payment.createcustomer.CreateCustomerResponse
import com.strings.cryptoapp.Response.BarcodeImageResponse
import com.strings.cryptoapp.Response.GenerateQrCodeResponnse
import com.strings.cryptoapp.Response.GetGAKeyByUserIResponse
import com.strings.cryptoapp.model.CreateUserGAKeyPayload
import com.strings.cryptoapp.model.Verify2FAPayload
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.io.File
import java.util.UUID

interface RestApi {
    @POST(Constants.login)
    fun addLogin(@Body loginPayload: LoginPayload): Call<LoginResponse>

    @POST(Constants.verifyLoginOtp)
    fun addVerifyLoginOtp(@Body verifyLoginOtpPayload: VerifyLoginOtpPayload): Call<SendRegistrationOtpResponce>

    @POST(Constants.sendLoginOtp)
    fun addSendLoginOtp(@Body sendLoginOtpPayload: SendLoginOtpPayload): Call<SendRegistrationOtpResponce>

    @POST(Constants.generateQrCode)
    fun addGenerateQrCode(@Body generateQrCodePayload: GenerateQrCodePayload): Call<BarcodeImageResponse>

//    @Multipart
//    @POST(Constants.register)
//    fun addRegister(@Part registerPayload: RegisterPayload): Call<RegisterResponse>

    @Multipart
    @POST(Constants.register)
    fun addRegister(
        @Part("FirstName") FirstName: RequestBody,
        @Part("LastName") LastName: RequestBody,
        @Part("Password") Password: RequestBody,
        @Part("CountryId") CountryId: RequestBody,
        @Part("Email") Email: RequestBody,
        @Part("PhoneNumber") PhoneNumber: RequestBody,
        @Part ProfileImage: MultipartBody.Part?,
        @Part("ImageURL") ImageURL: RequestBody?,
        @Part("CountryCode") CountryCode: RequestBody?,
        ): Call<RegisterResponse>

    @Multipart
    @POST(Constants.updateProfileDetail)
    fun updateProfileDetail(
        @Part("id") id: RequestBody,
        @Part("email") email: RequestBody,
        @Part("firstName") firstName: RequestBody,
        @Part("lastName") lastName: RequestBody,
        @Part ProfileImage: MultipartBody.Part?,
        @Part("phoneNumber") phoneNumber: RequestBody
    ): Call<Userupdatedsuccessfully>

//      @FormUrlEncoded
//    @POST(Constants.updateProfileDetail)
//    fun updateProfileDetail(
//        @Field("id") id: String,
//        @Field("email") email: String,
//        @Field("firstName") firstName: String,
//        @Field("lastName") lastName: String,
//        @Field("profileImage") profileImage: String,
//        @Field("phoneNumber") phoneNumber: String,
//        @Field("apiKey") apiKey: String,
//        @Field("secretKey") secretKey: String,
//    ): Call<Userupdatedsuccessfully>
//


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

//    @GET(Constants.strategy)
    @GET("http://103.14.99.42/api/StrategyApi/List")
    suspend fun getStrategy(): Response<StrategyRes>

    @GET(Constants.getProfileDetail)
    suspend fun getUserDetails(@Query("email") email: String): Response<UserDetailsResponse>

//    @GET("${Constants.strategy}/{id}")
//    suspend fun getStrategyById(@Path("id") id: String): Response<StrategyDetailRes>

    @GET(Constants.getBystrategyId)
    suspend fun getStrategyById(@Query("StrategyId") StrategyId: String): Response<StrategyDetailRes>

    @GET(Constants.getplans)
    suspend fun getPlans(): Response<GetPlanResponse>

    @POST(Constants.getusersubscription)
    fun addUserSubscription(@Body userSubscriptionModel: UserSubscriptionModel): Call<UserSubscriptionResponse>

    @POST(Constants.getSubscriptionDetails)
    fun addSubscriptionDetails(@Body userSubscriptionModel: UserSubscriptionModel): Call<UserSubscriptionDetail>

    @POST(Constants.createUserSubscription)
    fun addCreateUserSubscription(@Body createUserSubscriptionPayload: CreateUserSubscriptionPayload): Call<CmsAdsAddResponse>

    @POST(Constants.getOrderHistoryList)
    fun addOrderHistoryList(@Body getOrderHistoryListPayload: GetOrderHistoryListPayload): Call<OrderHistoriesResponse>

    @POST(Constants.getSubscriptionHistory)
    fun addSubscriptionHistory(@Body getOrderHistoryListPayload: GetOrderHistoryListPayload): Call<UserSubscriptionsResponse>

    @GET(Constants.cmsAdsList)
    suspend fun getCmsAdsList(@Query("userId") userId: String): Response<CmsAdsListResponse>

    @POST(Constants.cmsAdsAdd)
    fun addCmsAdsAdd(@Body cmsAdsAddPayload: CmsAdsAddPayload): Call<CmsAdsAddResponse>

    @POST(Constants.stripe_payment_customers_id)
    fun addCustomerIdCreate(): Call<CreateCustomerIdResponse>

    @Headers("Stripe-Version: 2022-11-15")
    @POST(Constants.stripe_payment_ephemeral_keys)
    fun addEhemeralkeys(@Query("customer") customer: String): Call<EphemeralKeyResponse>

    @POST(Constants.stripe_payment_intents)
    fun addStripePaymentIntents(
        @Query("customer") customer: String,
        @Query("amount") amount: String,
        @Query("currency") currency: String,
        @Query("setup_future_usage") setup_future_usage: String,
        @Query("automatic_payment_methods[enabled]") automatic_payment_methods: Boolean

    ): Call<PaymentIntentRespomse>

    @GET("${Constants.stripe_payment_intents}/{id}")
    suspend fun getStripePaymentId(@Path("id") id: String): Response<StripeConfirmationResponse>

    @POST(Constants.stripe_payment_intents)
    fun addCardPaymentIntents(
        @Query("amount") amount: String,
        @Query("currency") currency: String
    ): Call<CardPaymentIntentResponse>

    //CreateCustomer

//    @Headers({
//        "Content-Type:application/x-www-form-urlencode"
//        "Authorization:Bearer sk_test_51MFAOfSHmxsQH4CHc0B63ccrQu8tu1m9ynAXYaEydRrSQwp4nhBqKtJFaEYZn9aTYhsdw1Ti8VHA9Cw4ZRcZR8Lg00qUCjkGZk"
//    })

    @Headers(
        "Content-Type:application/x-www-form-urlencode",
        "Authorization: Bearer sk_test_51MFAOfSHmxsQH4CHc0B63ccrQu8tu1m9ynAXYaEydRrSQwp4nhBqKtJFaEYZn9aTYhsdw1Ti8VHA9Cw4ZRcZR8Lg00qUCjkGZk"
    )
    @POST(Constants.stripe_payment_customers_id)
    fun addCreateCustomer(
        @Query("email") email: String,
        @Query("name") name: String

    ): Call<CreateCustomerResponse>

    @POST(Constants.createUserGAKey)
    fun addCreateUserGAKey(@Body createUserGAKeyPayload: CreateUserGAKeyPayload): Call<GenerateQrCodeResponnse>

    @POST(Constants.verify2FA)
    fun addVerify2FA(@Body verify2FAPayload: Verify2FAPayload): Call<GenerateQrCodeResponnse>

    @GET(Constants.checkUserGAKey)
    suspend fun getCheckUserGAKey(@Query("userId") userId: String): Response<GenerateQrCodeResponnse>

    @GET(Constants.getGAKeyByUserId)
    suspend fun getGAKeyByUserId(@Query("userId") userId: String): Response<GetGAKeyByUserIResponse>

    @GET(Constants.getCountries)
    suspend fun getCountries(): Response<GetCountriesResponse>

    @GET(Constants.getDocuments)
    suspend fun getDocuments(): Response<DocumentResponse>

    @GET(Constants.getDocumentsByCountry)
    suspend fun getDocumentsByCountry(@Query("countryId") countryId: String): Response<DocumentResponse>

    @GET(Constants.getStrategy1OrderHistoryDetail)
    suspend fun getStrategy1OrderHistoryDetail(@Query("UserId") UserId: String): Response<OrderHistoryDetailResponse>

    @POST(Constants.createApiKeys)
    fun addCreateApiKeys(@Body createApiKeysPayload: CreateApiKeysPayload): Call<CmsAdsAddResponse>

    @GET(Constants.tradeSlotApi)
    suspend fun getTradeSlotApi(): Response<TradeSlotApiResponse>

    @POST(Constants.createTradeSlot)
    fun addCreateTradeSlot(@Body createTradeSlotPayload: CreateTradeSlotPayload): Call<CreateTradeSlotResponse>

    @GET(Constants.getLiveTopGainers)
    suspend fun getLiveTopGainers(@Query("UserId") UserId: String): Response<getLiveTopGainersResponse>

}
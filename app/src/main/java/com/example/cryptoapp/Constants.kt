package com.example.cryptoapp

import android.content.Context
import android.util.Log
import android.widget.Toast

class Constants {

    companion object {

        const val BASE_URL = "http://103.14.99.61:8084/api/"
        const val STRIPE_PAYMENT_URL = "https://api.stripe.com/v1/"

        // api routers
        const val login = "AccountsApi/login"
        const val register = "AccountsApi/register"
        const val sendRegistrationOtp = "AccountsApi/sendRegistrationOtp"
        const val verifyRegistrationOtp = "AccountsApi/verifyRegistrationOtp"
        const val verifyLoginOtp = "AccountsApi/verifyLoginOtp"
        const val sendLoginOtp = "AccountsApi/sendLoginOtp"

        const val forgotpassword = "AccountsApi/forgotpassword"
        const val resetpassword = "AccountsApi/resetpassword"
        const val registrationconfirm = "AccountsApi/registrationconfirm"

        const val getProfileDetail = "AccountsApi/getProfileDetail"
        const val updateProfileDetail = "AccountsApi/updateProfileDetail"

        const val cmsAdsList = "CMSAdsApi/CMSAdsList"
        const val cmsAdsAdd = "CMSAdsApi/UserCMSAdsAdd"

        const val strategy = "StrategyApi"
        const val getplans = "PlanApi/GetPlans"
        const val getusersubscription = "UserSubscriptionApi/GetSubscriptionList"
        const val getSubscriptionDetails = "UserSubscriptionApi/GetSubscriptionDetails"

        const val getOrderHistoryList = "HistoryApi/GetOrderHistoryList"
        const val getSubscriptionHistoryList = "HistoryApi/GetSubscriptionHistoryList"

        const val stripe_payment_customers_id= "customers"
        const val stripe_payment_ephemeral_keys = "ephemeral_keys"
        const val stripe_payment_intents = "payment_intents"

        const val cryptocurrencylist = "data-api/v3/cryptocurrency/listing?start=1&limit=10"

        const val CONNECT_TIMEOUT: Long = 60 * 1000 // 1 minutes
        const val READ_TIMEOUT: Long = 120 * 1000 // 2 minutes
        const val WRITE_TIMEOUT: Long = 120 * 1000 // 2 minutes

        const val ENGLISH_CODE = "en"
        const val ARABIC_CODE = "ar"
        const val DEFAULT_COUNTRY_CODE = "91"
        const val BEARER = "Bearer"
        const val PREFERENCES_NAME = "user"

        const val BROADCAST_REFRESH = "refresh"
        const val DISMISS_TO_LOGIN = "dismissToLogin"

        fun showLog(hint:String,log: String) {
            Log.d("test:-${hint}", log)
        }

        fun showToast(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

    }
}
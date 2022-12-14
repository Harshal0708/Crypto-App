package com.example.cryptoapp

class Constants {

    companion object {

        const val BASE_URL = "http://103.14.99.61:8084/api/"
        const val BASE_URL2 ="https://api.coinmarketcap.com/"

        // api routers
        const val login = "Accounts/login"
        const val register = "Accounts/register"
        const val forgotpassword = "Accounts/forgotpassword"
        const val resetpassword = "Accounts/resetpassword"
        const val registrationconfirm = "Accounts/registrationconfirm"
        const val resendotp = "Accounts/resendotp"
        const val userdetails = "Accounts/userdetails"

        const val strategy = "Strategy"
        const val getplans = "Plan/GetPlans"
        const val getusersubscription = "UserSubscription/GetSubscriptionList"
        const val getSubscriptionDetails = "UserSubscription/GetSubscriptionDetails"

        const val cryptocurrencylist = "data-api/v3/cryptocurrency/listing?start=1&limit=10"

        const val CONNECT_TIMEOUT : Long = 60 * 1000 // 1 minutes
        const val READ_TIMEOUT : Long = 120 * 1000 // 2 minutes
        const val WRITE_TIMEOUT : Long = 120 * 1000 // 2 minutes

        const val ENGLISH_CODE = "en"
        const val ARABIC_CODE = "ar"
        const val DEFAULT_COUNTRY_CODE = "91"
        const val BEARER = "Bearer"
        const val PREFERENCES_NAME = "user"

        const val BROADCAST_REFRESH = "refresh"
        const val DISMISS_TO_LOGIN = "dismissToLogin"

    }
}
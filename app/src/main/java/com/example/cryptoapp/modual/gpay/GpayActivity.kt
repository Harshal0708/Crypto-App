package com.example.cryptoapp.modual.gpay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.cryptoapp.Constants
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.card.CardPaymentIntentResponse
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.stripe.android.PaymentConfiguration
import com.stripe.android.googlepaylauncher.GooglePayEnvironment
import com.stripe.android.googlepaylauncher.GooglePayLauncher
import com.stripe.android.googlepaylauncher.GooglePayPaymentMethodLauncher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GpayActivity : AppCompatActivity() {


    var PUBLISH_KEY =
        "pk_test_51MFAOfSHmxsQH4CHWhNe1isqzDZRXBwVg1SWAwMr1FJ3qLc6xB7tPsfdqQnAyHFOQviqMFKwjH1ZjQYp7xNN7QzE00562CH1S5"
    var SECRET_KEY =
        "sk_test_51MFAOfSHmxsQH4CHc0B63ccrQu8tu1m9ynAXYaEydRrSQwp4nhBqKtJFaEYZn9aTYhsdw1Ti8VHA9Cw4ZRcZR8Lg00qUCjkGZk"

    var amount = 11

    private lateinit var clientSecret: String
    private lateinit var googlePayButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gpay)

        PaymentConfiguration.init(this, PUBLISH_KEY)
        googlePayButton = findViewById(R.id.googlePayButton)
       // getClientSecretKey()
        googlePay()

    }

    private fun getClientSecretKey() {

        val response = ServiceBuilder(this@GpayActivity,true).buildService(RestApi::class.java)

        response.addCardPaymentIntents("${amount}00","INR")
            .enqueue(
                object : Callback<CardPaymentIntentResponse> {
                    override fun onResponse(
                        call: Call<CardPaymentIntentResponse>,
                        response: Response<CardPaymentIntentResponse>
                    )
                    {
                        clientSecret=response.body()?.client_secret.toString()
                        Constants.showLog("clientSecretId", clientSecret)
                    }

                    override fun onFailure(call: Call<CardPaymentIntentResponse>, t: Throwable) {

                    }
                }
            )
    }

    private fun googlePay() {
//
//        val googlePayLauncher = GooglePayPaymentMethodLauncher(
//            activity = this,
//            config = GooglePayPaymentMethodLauncher.Config(
//                environment = GooglePayEnvironment.Test,
//                merchantCountryCode = "IND",
//                merchantName = "Widget Store"
//            ),
//            readyCallback = ::onGooglePayReady,
//            resultCallback = ::onGooglePayResult
//        )

//        googlePayButton.setOnClickListener {
//            googlePayLauncher.present(
//                currencyCode = "INR",
//                amount = amount
//            )
//        }


        val googlePayLauncher = GooglePayLauncher(
            activity = this,
            config = GooglePayLauncher.Config(
                environment = GooglePayEnvironment.Test,
                merchantCountryCode = "IN",
                merchantName = "MerchantName"
            ),
            readyCallback = ::onGooglePayReady,
            resultCallback = ::onGooglePayResult
        )

        //   clientSecret=""
        googlePayButton.setOnClickListener {
            // launch `GooglePayLauncher` to confirm a Payment Intent
            googlePayLauncher.presentForPaymentIntent(clientSecret)
        }

    }

    private fun onGooglePayResult(result: GooglePayLauncher.Result) {
        // implemented below
        showLog("onGooglePayResult",result.toString())
        showToast(this@GpayActivity,result.toString())
    }

    private fun onGooglePayReady(isReady: Boolean) {
        googlePayButton.isEnabled = isReady
    }

    private fun onGooglePayResult(
        result: GooglePayPaymentMethodLauncher.Result
    ) {
        when (result) {

            is GooglePayPaymentMethodLauncher.Result.Completed -> {
                // Payment details successfully captured.
                // Send the paymentMethodId to your server to finalize payment.
                val paymentMethodId = result.paymentMethod.id
                showToast(this@GpayActivity,paymentMethodId.toString())
            }

            GooglePayPaymentMethodLauncher.Result.Canceled -> {
                // User canceled the operation
            }

            is GooglePayPaymentMethodLauncher.Result.Failed -> {
                // Operation failed; inspect `result.error` for the exception
            }

        }
    }
}
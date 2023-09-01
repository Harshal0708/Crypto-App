package com.example.cryptoapp.modual.card

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.cryptoapp.Constants
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.payment.PaymentIntentRespomse
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import com.stripe.android.view.CardInputWidget
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardActivity : AppCompatActivity() {

    var PUBLISH_KEY =
        "pk_test_51MFAOfSHmxsQH4CHWhNe1isqzDZRXBwVg1SWAwMr1FJ3qLc6xB7tPsfdqQnAyHFOQviqMFKwjH1ZjQYp7xNN7QzE00562CH1S5"
    var SECRET_KEY =
        "sk_test_51MFAOfSHmxsQH4CHc0B63ccrQu8tu1m9ynAXYaEydRrSQwp4nhBqKtJFaEYZn9aTYhsdw1Ti8VHA9Cw4ZRcZR8Lg00qUCjkGZk"

    var amount = 1099

    private lateinit var clientSecretId: String
    private lateinit var paymentIntentClientSecret: String
    private lateinit var stripe: Stripe
    private lateinit var paymentLauncher: PaymentLauncher
    private lateinit var cardInputWidget : CardInputWidget
    private lateinit var payButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        cardInputWidget = findViewById(R.id.cardInputWidget)
        payButton = findViewById(R.id.payButton)

        PaymentConfiguration.init(
            applicationContext,
            PUBLISH_KEY
        )

        stripe =Stripe(this,PaymentConfiguration.getInstance(applicationContext).publishableKey)

        val paymentConfiguration = PaymentConfiguration.getInstance(applicationContext)
        paymentLauncher = PaymentLauncher.Companion.create(
            this,
            paymentConfiguration.publishableKey,
            paymentConfiguration.stripeAccountId,
            ::onPaymentResult
        )

        getClientSecretKey()
    }
    private fun startCheckout() {


        // Confirm the PaymentIntent with the card widget
        payButton.setOnClickListener {
            cardInputWidget.paymentMethodCreateParams?.let { params ->
                val confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(params, clientSecretId)
                lifecycleScope.launch {
                    paymentLauncher.confirm(confirmParams)
                }
            }
        }
    }


    private fun getClientSecretKey() {
        val response = ServiceBuilder(this@CardActivity,true).buildService(RestApi::class.java)

        response.addCardPaymentIntents("${amount}00","INR")
            .enqueue(
                object : Callback<CardPaymentIntentResponse> {
                    override fun onResponse(
                        call: Call<CardPaymentIntentResponse>,
                        response: Response<CardPaymentIntentResponse>
                    ) {
                        clientSecretId=response.body()?.client_secret.toString()
                        showLog("clientSecretId", clientSecretId)
                        showLog("response", response.body()!!.id)

                        startCheckout()
                    }

                    override fun onFailure(call: Call<CardPaymentIntentResponse>, t: Throwable) {

                    }
                }
            )
    }


    private fun onPaymentResult(paymentResult: PaymentResult) {
        val message = when (paymentResult) {
            is PaymentResult.Completed -> {
                "Completed!"

                showToast(this@CardActivity,"Completed!")
            }
            is PaymentResult.Canceled -> {
                "Canceled!"
                showToast(this@CardActivity,"Canceled!")
            }
            is PaymentResult.Failed -> {
                // This string comes from the PaymentIntent's error message.
                // See here: https://stripe.com/docs/api/payment_intents/object#payment_intent_object-last_payment_error-message
                "Failed: " + paymentResult.throwable.message
                showToast(this@CardActivity,paymentResult.throwable.message.toString())
            }
        }
        showToast(this@CardActivity,"Payment Result:${message}",)
        showLog("onPaymentResult",message.toString())
    }
}

package com.example.cryptoapp.modual.gpay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.stripe.android.PaymentConfiguration
import com.stripe.android.core.networking.AnalyticsFields.PUBLISHABLE_KEY
import com.stripe.android.googlepaylauncher.GooglePayEnvironment
import com.stripe.android.googlepaylauncher.GooglePayLauncher

class GpayTwoActivity : AppCompatActivity() {
    var PUBLISH_KEY =
        "pk_test_51MFAOfSHmxsQH4CHWhNe1isqzDZRXBwVg1SWAwMr1FJ3qLc6xB7tPsfdqQnAyHFOQviqMFKwjH1ZjQYp7xNN7QzE00562CH1S5"
    var SECRET_KEY =
        "sk_test_51MFAOfSHmxsQH4CHc0B63ccrQu8tu1m9ynAXYaEydRrSQwp4nhBqKtJFaEYZn9aTYhsdw1Ti8VHA9Cw4ZRcZR8Lg00qUCjkGZk"

    private lateinit var clientSecret: String

    private lateinit var googlePayButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gpay_two)

        //PaymentConfiguration.init(this, PUBLISHABLE_KEY)
        PaymentConfiguration.init(this, PUBLISH_KEY)

        googlePayButton = findViewById(R.id.googlePayButton)

        val googlePayLauncher = GooglePayLauncher(
            activity = this,
            config = GooglePayLauncher.Config(
                environment = GooglePayEnvironment.Test,
                merchantCountryCode = "US",
                merchantName = "Widget Store"
            ),
            readyCallback = ::onGooglePayReady,
            resultCallback = ::onGooglePayResult
        )

        googlePayButton.setOnClickListener {
            // launch `GooglePayLauncher` to confirm a Payment Intent
            googlePayLauncher.presentForPaymentIntent(clientSecret)
        }
    }

    private fun onGooglePayReady(isReady: Boolean) {
        // implemented below
        googlePayButton.isEnabled=isReady
        showToast(this@GpayTwoActivity,this@GpayTwoActivity,isReady.toString())
    }

    private fun onGooglePayResult(result: GooglePayLauncher.Result) {
        showToast(this@GpayTwoActivity,this@GpayTwoActivity,result.toString())
    }
    
}

package com.example.cryptoapp.modual.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.Constants
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.UserSubscriptionsResponse
import com.example.cryptoapp.model.GetOrderHistoryListPayload
import com.example.cryptoapp.modual.history.adapter.SubscriptionHistoryAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.stripe.android.PaymentConfiguration
import com.stripe.android.core.injection.PUBLISHABLE_KEY
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var pay: Button
    lateinit var payment_price: TextView
    lateinit var payment_id: TextView
    var PUBLISH_KEY =
        "pk_test_51MFAOfSHmxsQH4CHWhNe1isqzDZRXBwVg1SWAwMr1FJ3qLc6xB7tPsfdqQnAyHFOQviqMFKwjH1ZjQYp7xNN7QzE00562CH1S5"
    var SECRET_KEY =
        "sk_test_51MFAOfSHmxsQH4CHc0B63ccrQu8tu1m9ynAXYaEydRrSQwp4nhBqKtJFaEYZn9aTYhsdw1Ti8VHA9Cw4ZRcZR8Lg00qUCjkGZk"
    lateinit var paymentSheet: PaymentSheet
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    lateinit var customerId: String
    lateinit var ephemeralId: String
    lateinit var clientSecretId: String

    lateinit var confirmId: String
    var amount=16
    lateinit var googlePayConfiguration : PaymentSheet.GooglePayConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        InIt()
    }

    private fun InIt() {
        pay = findViewById(R.id.pay)
        payment_price = findViewById(R.id.payment_price)
        payment_id = findViewById(R.id.payment_id)
        pay.setOnClickListener(this)

        GooglePayInit()
        StripePayment()
        getPaymentList()
    }

    private fun GooglePayInit() {
        googlePayConfiguration = PaymentSheet.GooglePayConfiguration(
            environment = PaymentSheet.GooglePayConfiguration.Environment.Test,
            countryCode = "US",
            currencyCode = "USD" // Required for Setup Intents, optional for Payment Intents
        )

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.pay -> {
                presentPaymentSheet(clientSecretId,ephemeralId)
            }
        }
    }

    private fun StripePayment() {
        PaymentConfiguration.init(this, PUBLISH_KEY)
        paymentSheet = PaymentSheet(this@PaymentActivity, ::onPaymentSheetResult)
    }


    fun getPaymentList() {

        val response = ServiceBuilder(this@PaymentActivity).buildService(RestApi::class.java)

        response.addCustomerIdCreate()
            .enqueue(
                object : Callback<CreateCustomerIdResponse> {
                    override fun onResponse(
                        call: Call<CreateCustomerIdResponse>,
                        response: Response<CreateCustomerIdResponse>
                    ) {
                        customerId=response.body()?.id.toString()
                        showToast(this@PaymentActivity,customerId)
                        showLog("customerId", customerId)
                        getEphemeral_keys(customerId)
                    }
                    override fun onFailure(call: Call<CreateCustomerIdResponse>, t: Throwable) {

                    }
                }
            )
    }

    private fun getEphemeral_keys(key: String) {
        val response = ServiceBuilder(this@PaymentActivity).buildService(RestApi::class.java)

        response.addEhemeralkeys(key)
            .enqueue(
                object : Callback<EphemeralKeyResponse> {
                    override fun onResponse(
                        call: Call<EphemeralKeyResponse>,
                        response: Response<EphemeralKeyResponse>
                    ) {
                        ephemeralId=response.body()?.id.toString()
                        showToast(this@PaymentActivity,ephemeralId)
                        showLog( "ephemeralId", ephemeralId)
                        getClientSecretKey(customerId)
                    }

                    override fun onFailure(call: Call<EphemeralKeyResponse>, t: Throwable) {
                    }
                }
            )
    }

    private fun getClientSecretKey(key: String) {
        val response = ServiceBuilder(this@PaymentActivity).buildService(RestApi::class.java)

        response.addStripePaymentIntents(key,"${amount}00","INR","true")
            .enqueue(
                object : Callback<PaymentIntentRespomse> {
                    override fun onResponse(
                        call: Call<PaymentIntentRespomse>,
                        response: Response<PaymentIntentRespomse>
                    ) {
                        clientSecretId=response.body()?.client_secret.toString()
                        confirmId=response.body()!!.id
                        showLog( "clientSecretId", clientSecretId)
                        showLog("response",response.body()!!.id)
                        showToast(this@PaymentActivity,clientSecretId)
                    }

                    override fun onFailure(call: Call<PaymentIntentRespomse>, t: Throwable) {

                    }
                }
            )
    }


    fun presentPaymentSheet(clientSecretId: String,ephemeralId:String) {

        paymentSheet.presentWithPaymentIntent(
            clientSecretId,
            PaymentSheet.Configuration(
                merchantDisplayName = "Abc Company",
                // Set `allowsDelayedPaymentMethods` to true if your business
                // can handle payment methods that complete payment after a delay, like SEPA Debit and Sofort.
                allowsDelayedPaymentMethods = true,
                googlePay = googlePayConfiguration

            )

        )
        customerConfig = PaymentSheet.CustomerConfiguration(
           clientSecretId,
            ephemeralId
        )
    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                print("Canceled")
                showToast(this@PaymentActivity,"Canceled")
                showLog("Canceled",paymentSheetResult.toString())
            }
            is PaymentSheetResult.Failed -> {
                print("Error: ${paymentSheetResult.error}")
                showToast(this@PaymentActivity,paymentSheetResult.error.toString())
                showLog("Error",paymentSheetResult.toString())
            }
            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                print("Completed")
                showToast(this@PaymentActivity,"Completed")
                getStripePaymentId(confirmId)
                showLog("Completed",paymentSheetResult.toString())
            }
        }
    }

    private fun getStripePaymentId(confirmId: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            var response = ServiceBuilder(this@PaymentActivity).buildService(RestApi::class.java).getStripePaymentId(confirmId)
            withContext(Dispatchers.Main) {
                payment_price.text="${response.body()?.amount.toString()} ${response.body()?.currency}"
                payment_id.text="ID: ${response.body()?.id} \n customer id: ${response.body()?.customer}"
            }
        }
    }
}

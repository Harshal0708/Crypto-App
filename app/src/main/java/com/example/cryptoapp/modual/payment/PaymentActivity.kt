package com.example.cryptoapp.modual.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import com.example.cryptoapp.modual.payment.createcustomer.CreateCustomerResponse
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
import java.util.Objects

class PaymentActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var pay: Button
    lateinit var addCustomer: Button
    lateinit var payment_price: TextView
    lateinit var payment_id: TextView
    lateinit var name: EditText
    lateinit var email: EditText
    var PUBLISH_KEY =
        "pk_test_51MFAOfSHmxsQH4CHWhNe1isqzDZRXBwVg1SWAwMr1FJ3qLc6xB7tPsfdqQnAyHFOQviqMFKwjH1ZjQYp7xNN7QzE00562CH1S5"
    var SECRET_KEY =
        "sk_test_51MFAOfSHmxsQH4CHc0B63ccrQu8tu1m9ynAXYaEydRrSQwp4nhBqKtJFaEYZn9aTYhsdw1Ti8VHA9Cw4ZRcZR8Lg00qUCjkGZk"
    lateinit var paymentSheet: PaymentSheet
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    var customerId: String = ""
    var ephemeralId: String = ""
    var clientSecretId: String = ""

    lateinit var confirmId: String
    var amount = 16
    lateinit var googlePayConfiguration: PaymentSheet.GooglePayConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        InIt()
    }

    private fun InIt() {
        pay = findViewById(R.id.pay)
        addCustomer = findViewById(R.id.addCustomer)
        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        payment_price = findViewById(R.id.payment_price)
        payment_id = findViewById(R.id.payment_id)
        pay.setOnClickListener(this)
        addCustomer.setOnClickListener(this)

//        GooglePayInit()
        StripePayment()
        getPaymentList()
    }

    private fun GooglePayInit() {
        googlePayConfiguration = PaymentSheet.GooglePayConfiguration(
            environment = PaymentSheet.GooglePayConfiguration.Environment.Production,
            countryCode = "IN",
            currencyCode = "INR" // Required for Setup Intents, optional for Payment Intents
        )
    }
//
    override fun onClick(p0: View?) {
        when (p0?.id) {
//            R.id.pay -> {
//                if (clientSecretId != "" && ephemeralId != "") {
//                    presentPaymentSheet(clientSecretId, ephemeralId)
//                } else {
//                    showToast(this@PaymentActivity,this@PaymentActivity, "clientSecretId & ephemeralId not created")
//                }
//            }
//
//            R.id.addCustomer -> {
//                addCustomer()
//            }



        }
    }


    fun addCustomer() {

        val response = ServiceBuilder(this@PaymentActivity,true).buildService(RestApi::class.java)

        response.addCreateCustomer("apurva.skyttus@gmail.com","apurva")
            .enqueue(
                object : Callback<CreateCustomerResponse> {
                    override fun onResponse(
                        call: Call<CreateCustomerResponse>,
                        response: Response<CreateCustomerResponse>
                    ) {

                        response.body()?.let { showToast(this@PaymentActivity,this@PaymentActivity, it.email) }
                        showLog("customerId", response.body().toString())
                        if (clientSecretId != "" && ephemeralId != "") {
                            presentPaymentSheet(clientSecretId, ephemeralId)
                        } else {
                            showToast(this@PaymentActivity, this@PaymentActivity,"clientSecretId & ephemeralId not created")
                        }
                    }

                    override fun onFailure(call: Call<CreateCustomerResponse>, t: Throwable) {
                        showToast(this@PaymentActivity, this@PaymentActivity,t.toString())
                    }
                }
            )
    }

    private fun StripePayment() {
        PaymentConfiguration.init(this, PUBLISH_KEY)
        paymentSheet = PaymentSheet(this@PaymentActivity, ::onPaymentSheetResult)
    }


    fun getPaymentList() {

        val response = ServiceBuilder(this@PaymentActivity,true).buildService(RestApi::class.java)

        response.addCustomerIdCreate()
            .enqueue(
                object : Callback<CreateCustomerIdResponse> {
                    override fun onResponse(
                        call: Call<CreateCustomerIdResponse>,
                        response: Response<CreateCustomerIdResponse>
                    ) {

                        customerId = response.body()?.id.toString()
                        showToast(this@PaymentActivity,this@PaymentActivity, customerId)
                        showLog("customerId", customerId)

                        if (customerId != "") {
                            getEphemeral_keys(customerId)
                        } else {
                            showToast(this@PaymentActivity, this@PaymentActivity,"customerId not created")
                        }

                    }

                    override fun onFailure(call: Call<CreateCustomerIdResponse>, t: Throwable) {
                        showToast(this@PaymentActivity,this@PaymentActivity, t.toString())
                    }
                }
            )
    }

    private fun getEphemeral_keys(key: String) {
        val response = ServiceBuilder(this@PaymentActivity,true).buildService(RestApi::class.java)

        response.addEhemeralkeys(key)
            .enqueue(
                object : Callback<EphemeralKeyResponse> {
                    override fun onResponse(
                        call: Call<EphemeralKeyResponse>,
                        response: Response<EphemeralKeyResponse>
                    )
                    {

                        ephemeralId = response.body()?.id.toString()
                        showToast(this@PaymentActivity,this@PaymentActivity, ephemeralId)
                        showLog("ephemeralId", ephemeralId)

                        if (ephemeralId != "") {
                            getClientSecretKey(customerId)
                        } else {
                            showToast(this@PaymentActivity, this@PaymentActivity,"ephemeralId not created")
                        }

                    }

                    override fun onFailure(call: Call<EphemeralKeyResponse>, t: Throwable) {
                        showToast(this@PaymentActivity,this@PaymentActivity, t.toString())
                    }
                }
            )
    }

    private fun getClientSecretKey(key: String) {
        val response = ServiceBuilder(this@PaymentActivity,true).buildService(RestApi::class.java)

        response.addStripePaymentIntents(key, "${amount}00", "INR", "off_session",true)
            .enqueue(
                object : Callback<PaymentIntentRespomse> {
                    override fun onResponse(
                        call: Call<PaymentIntentRespomse>,
                        response: Response<PaymentIntentRespomse>
                    ) {
                        clientSecretId = response.body()?.client_secret.toString()
                        confirmId = response.body()!!.id
                        showLog("clientSecretId", clientSecretId)
                        showLog("response", response.body()!!.id)
                        showToast(this@PaymentActivity,this@PaymentActivity, clientSecretId)
                        addCustomer()
                    }

                    override fun onFailure(call: Call<PaymentIntentRespomse>, t: Throwable) {
                        showToast(this@PaymentActivity,this@PaymentActivity, t.toString())
                    }
                }
            )
    }


    fun presentPaymentSheet(clientSecretId: String, ephemeralId: String) {

        customerConfig = PaymentSheet.CustomerConfiguration(
            clientSecretId,
            ephemeralId
        )

        paymentSheet.presentWithPaymentIntent(
            clientSecretId,
            PaymentSheet.Configuration(
                merchantDisplayName = "Abc Company",
                customer =customerConfig,
                // Set `allowsDelayedPaymentMethods` to true if your business
                // can handle payment methods that complete payment after a delay, like SEPA Debit and Sofort.
                allowsDelayedPaymentMethods = true,
                //googlePay = googlePayConfiguration,

            )

        )

    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                print("Canceled")
                showToast(this@PaymentActivity, this@PaymentActivity,"Canceled")
                showLog("Canceled", paymentSheetResult.toString())
            }
            is PaymentSheetResult.Failed -> {
                print("Error: ${paymentSheetResult.error}")
                showToast(this@PaymentActivity,this@PaymentActivity, paymentSheetResult.error.toString())
                showLog("Error", paymentSheetResult.toString())
            }
            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                print("Completed")
                showToast(this@PaymentActivity,this@PaymentActivity, "Completed")
                if (confirmId != "") {
                    getStripePaymentId(confirmId)
                }
                showLog("Completed", paymentSheetResult.toString())
            }
        }
    }

    private fun getStripePaymentId(confirmId: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            var response = ServiceBuilder(this@PaymentActivity,true).buildService(RestApi::class.java)
                .getStripePaymentId(confirmId)
            withContext(Dispatchers.Main) {
                showLog("getStripePaymentId",response.body().toString())
                payment_price.text =
                    "${response.body()?.amount.toString()} ${response.body()?.currency}"
                payment_id.text =
                    "ID: ${response.body()?.id} \n customer id: ${response.body()?.customer}"

//               var policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//                StrictMode.setThreadPolicy(policy)
            }
        }
    }
}
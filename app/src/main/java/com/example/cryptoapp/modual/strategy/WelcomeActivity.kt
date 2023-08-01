package com.example.cryptoapp.modual.strategy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.service.autofill.Dataset
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.CmsAdsAddResponse
import com.example.cryptoapp.Response.CreateTradeSlotResponse
import com.example.cryptoapp.model.CreateApiKeysPayload
import com.example.cryptoapp.model.CreateTradeSlotPayload
import com.example.cryptoapp.modual.login.ForgotPasswordActivity
import com.example.cryptoapp.modual.strategy.adapter.CoinData
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.google.gson.Gson
import com.google.gson.JsonArray
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import java.util.*
import kotlin.collections.ArrayList
class WelcomeActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var view: View
    lateinit var register_progressBar: ProgressBar
    lateinit var resent: TextView
    lateinit var progressBar_cardView: RelativeLayout

    var coinSelectList: ArrayList<CoinData> = ArrayList()
    var coin_selection: String = ""
    var slider_price: Int = -1
    var tradingType: Int = -1
    var strategyId: String = ""
    var userId: String = ""
    var coinSeparatedString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)
        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)
        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.next)
        progressBar_cardView.setOnClickListener(this)

        val numberList = intent.getSerializableExtra("data")
        coin_selection = intent.getStringExtra("coin_selection").toString()
        slider_price = intent.extras!!.getInt("slider_price", -1)
        tradingType = intent.getIntExtra("tradingType", -1)
        strategyId = intent.getStringExtra("strategyId").toString()
        userId = intent.getStringExtra("userId").toString()

        coinSelectList = numberList as ArrayList<CoinData>
        coinSeparatedString = concatenateStrings(coinSelectList)
    }

    fun concatenateStrings(list: ArrayList<CoinData>): String {
        val stringBuilder = StringBuilder()
        for (item in list) {
            stringBuilder.append(item.coin).append(",")
        }
        return stringBuilder.toString()
    }

    private fun addCreateApiKeys() {
        register_progressBar.visibility = View.VISIBLE


        var date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formattedDate: String = formatter.format(date)


        val response =
            this?.let { ServiceBuilder(it).buildService(RestApi::class.java) }

        val payload = CreateTradeSlotPayload(
            coinSeparatedString.trimEnd(','),
            formattedDate,
            formattedDate,
            slider_price,
            coin_selection,
            strategyId,
            tradingType,
            userId
        )

        Constants.showLog("test", Gson().toJson(payload))

//        response!!.addCreateTradeSlot(payload)
//            .enqueue(object : retrofit2.Callback<CreateTradeSlotResponse> {
//                override fun onResponse(
//                    call: retrofit2.Call<CreateTradeSlotResponse>,
//                    response: retrofit2.Response<CreateTradeSlotResponse>
//                ) {
//
//                    if (response.body()?.isSuccess == true) {
//                        Constants.showToast(this@WelcomeActivity, "Welcome")
//                    }else{
//                        Constants.showToast(this@WelcomeActivity, "Failed...")
//                    }
//                    register_progressBar.visibility = View.GONE
//
//
//                }
//
//                override fun onFailure(
//                    call: retrofit2.Call<CreateTradeSlotResponse>,
//                    t: Throwable
//                ) {
//
//                    register_progressBar.visibility = View.GONE
//                    Constants.showToast(
//                        this@WelcomeActivity,
//                        getString(R.string.login_failed)
//                    )
//                }
//            })

    }


    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {

            R.id.progressBar_cardView -> {
                //addCreateApiKeys()
            }


        }
    }

}



package com.example.cryptoapp.modual.strategy

import android.os.Bundle
import android.service.autofill.Dataset
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.CmsAdsAddResponse
import com.example.cryptoapp.model.CreateApiKeysPayload
import com.example.cryptoapp.model.CreateTradeSlotPayload
import com.example.cryptoapp.modual.strategy.adapter.CoinData
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.google.gson.Gson
import com.google.gson.JsonArray

class WelcomeActivity : AppCompatActivity() {

    var coinSelectList : ArrayList<CoinData> = ArrayList()

    var coin_selection: String = ""
    var slider_price: String = ""
    var tradingType: Int = -1
    var strategyId: String = ""
    var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val numberList = intent .getSerializableExtra( "data")
        coin_selection = intent .getStringExtra( "coin_selection").toString()
        slider_price = intent .getStringExtra( "slider_price").toString()
        tradingType = intent .getIntExtra( "tradingType",-1)
        strategyId = intent .getStringExtra( "strategyId").toString()
        userId = intent .getStringExtra( "userId").toString()

        coinSelectList = numberList as ArrayList<CoinData>

        for (i in coinSelectList){
            Constants.showLog("dataXX", i.coin)
        }

    }

    private fun addCreateApiKeys() {
        //register_progressBar.visibility = View.VISIBLE

        val response =
            this?.let { ServiceBuilder(it).buildService(RestApi::class.java) }
//
//        val coins: String,
//        val createdDate: String,
//        val modifiedDate: String,
//        val price: Int,
//        val slotId: String,
//        val strategyId: String,
//        val tradingType: Int,
//        val userId: String

        val payload = CreateTradeSlotPayload(
            "BTCUSDT,ETHBTC",
            "2023-07-27T11:12:52.569",
            "2023-07-27T11:12:52.569Z",
            slider_price.toInt(),
            coin_selection,
            strategyId,
            tradingType,
            userId
        )

        Constants.showLog("test",payload.toString())
//        response!!.addCreateTradeSlot(payload)
//            .enqueue(object : retrofit2.Callback<CmsAdsAddResponse> {
//                override fun onResponse(
//                    call: retrofit2.Call<CmsAdsAddResponse>,
//                    response: retrofit2.Response<CmsAdsAddResponse>
//                ) {
//
//                    if (response.body()?.isSuccess == true) {
//                        Constants.showToast(this@WelcomeActivity,"Welcome")
//                    }
//
//                }
//                override fun onFailure(
//                    call: retrofit2.Call<CmsAdsAddResponse>,
//                    t: Throwable
//                ) {
//
//                    //register_progressBar.visibility = View.GONE
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
}



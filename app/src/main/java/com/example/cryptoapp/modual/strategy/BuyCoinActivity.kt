package com.example.cryptoapp.modual.strategy

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXXXX
import com.example.cryptoapp.modual.strategy.adapter.BuyCoinAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.network.onItemClickListener
import com.google.android.material.slider.Slider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat

class BuyCoinActivity : AppCompatActivity(), onItemClickListener {

    lateinit var rv_buy_coin: RecyclerView
    lateinit var buyCoinAdapter: BuyCoinAdapter
    lateinit var data: List<DataXXXX>

    private lateinit var continuousSlider: Slider
    var tradingType: Int = -1
    var strategyId: String = ""
    var userId: String = ""
    var slider_price: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_coin)

        init()
    }

    fun init() {
        rv_buy_coin = findViewById(R.id.rv_buy_coin)
        continuousSlider = findViewById(R.id.continuousSlider)

        if(intent.extras != null){
            tradingType = intent.getIntExtra("tradingType",-1)
            strategyId = intent.getStringExtra("strategyId").toString()
            userId = intent.getStringExtra("userId").toString()

            showLog("tradingType", tradingType.toString())
            showLog("strategyId", strategyId)
            showLog("userId", userId)

        }

        continuousSlider.setLabelFormatter { value: Float ->
            val format = NumberFormat.getInstance()
            format.maximumFractionDigits = 0
//            format.currency = Currency.getInstance("USD")
            format.format(value.toDouble())
        }


        continuousSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started

            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped

            }
        })

        continuousSlider.addOnChangeListener(object : Slider.OnChangeListener {
            @SuppressLint("RestrictedApi")
            override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
                //Log.d("test", slider.value.toString())
                slider_price = slider.value.toInt()
            }
        })
        getTradeSlotApi()
    }

    private fun getTradeSlotApi() {
        // viewLoader.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var response = ServiceBuilder(this@BuyCoinActivity).buildService(RestApi::class.java)
                .getTradeSlotApi()
            withContext(Dispatchers.Main) {
                //viewLoader.visibility = View.GONE

                data = response.body()!!.data
                rv_buy_coin.layoutManager = LinearLayoutManager(this@BuyCoinActivity)
                buyCoinAdapter = BuyCoinAdapter(
                    this@BuyCoinActivity,
                    data,
                    onItemClickListener = this@BuyCoinActivity,
                    BuyCoinActivity()
                )
                rv_buy_coin.adapter = buyCoinAdapter

            }
        }
    }

    override fun onItemClick(pos: Int) {

        if(slider_price == -1){
            showToast(this@BuyCoinActivity,"Please select price")
        }else{
            val intent = Intent(this@BuyCoinActivity, CoinSelectionActivity::class.java)
            intent.putExtra("coin_selection", data.get(pos).id)
            intent.putExtra("slider_price", slider_price)
            intent.putExtra("tradingType", tradingType)
            intent.putExtra("strategyId", strategyId)
            intent.putExtra("userId", userId)
            startActivity(intent)
            this.finish()
        }
    }
}

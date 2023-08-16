package com.example.cryptoapp.modual.strategy

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var rvBuyCoin: RecyclerView
    private lateinit var buyCoinAdapter: BuyCoinAdapter
    private lateinit var data: List<DataXXXX>

    private lateinit var continuousSlider: Slider
    var tradingType: Int = -1
    var strategyId: String = ""
    var userId: String = ""
    private var sliderPrice: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_coin)

        init()
    }

    fun init() {
        rvBuyCoin = findViewById(R.id.rvBuyCoin)
        continuousSlider = findViewById(R.id.continuousSlider)

        if (intent.extras != null) {
            tradingType = intent.getIntExtra("tradingType", -1)
            strategyId = intent.getStringExtra("strategyId").toString()
            userId = intent.getStringExtra("userId").toString()
        } else {
            showToast(this@BuyCoinActivity, getString(R.string.something_wrong))
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

            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {

            }
        })

        continuousSlider.addOnChangeListener(
            Slider.OnChangeListener
            { slider, value, fromUser ->
                sliderPrice = slider.value.toInt()
            })
        getTradeSlotApi()
    }

    private fun getTradeSlotApi() {
        // viewLoader.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder(this@BuyCoinActivity).buildService(RestApi::class.java)
                .getTradeSlotApi()
            withContext(Dispatchers.Main) {
                //viewLoader.visibility = View.GONE

                if (response.body()!!.isSuccess) {
                    data = response.body()!!.data
                    rvBuyCoin.layoutManager = LinearLayoutManager(this@BuyCoinActivity)
                    buyCoinAdapter = BuyCoinAdapter(
                        this@BuyCoinActivity,
                        data,
                        onItemClickListener = this@BuyCoinActivity,
                        BuyCoinActivity(),
                    )
                    rvBuyCoin.adapter = buyCoinAdapter
                } else {
                    showToast(this@BuyCoinActivity, getString(R.string.data_not_found))
                }


            }
        }
    }

    override fun onItemClick(pos: Int) {
        if (sliderPrice == -1) {
            showToast(this@BuyCoinActivity, resources.getString(R.string.Please_select_price))
            return
        } else if (data[pos].endValue > sliderPrice) {
            showToast(this@BuyCoinActivity, resources.getString(R.string.Please_other_select_price))
            return
        } else {
            sendData(tradingType, pos)
        }
    }

    private fun sendData(tradingType: Int, pos: Int) {
        val intent : Intent
        if(tradingType == 0){
            intent = Intent(this@BuyCoinActivity, WelcomeActivity::class.java)
            intent.putExtra("data", "")
        }else{
            intent = Intent(this@BuyCoinActivity, CoinSelectionActivity::class.java)
        }
        intent.putExtra("coin_selection", data[pos].id)
        intent.putExtra("slider_price", sliderPrice)
        intent.putExtra("tradingType", tradingType)
        intent.putExtra("strategyId", strategyId)
        intent.putExtra("userId", userId)
        startActivity(intent)
        this.finish()
    }
}
package com.example.cryptoapp.modual.history

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.PositionDetailResponseItem
import com.example.cryptoapp.pagination.Utility.showErrorToast
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class PositionDetailActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var client: OkHttpClient
    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var job1: Job
    private val THREAD_POOL_SIZE = 10
    private val executorService: ExecutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE)
    lateinit var webSocket1: WebSocket

    lateinit var orderId: String
    lateinit var txt_sd_strategyName: TextView
    lateinit var txt_detail_sd_description: TextView
    lateinit var txt_strategy_number: TextView
    lateinit var txt_strategy_quantity: TextView
    lateinit var txt_strategy_om: TextView
    lateinit var txt_strategy_symbol: TextView
    lateinit var txt_strategy_date: TextView
    lateinit var txt_strategy_tdt: TextView
    lateinit var txt_strategy_tt: TextView
    lateinit var txt_buy_price: TextView
    lateinit var txt_target_price: TextView
    lateinit var txt_sl_price: TextView
    lateinit var txt_strategy_pl: TextView

    lateinit var view: View
    lateinit var register_progressBar: ProgressBar
    lateinit var resent: TextView


    lateinit var view2: View
    lateinit var register_progressBar2: ProgressBar
    lateinit var resent2: TextView

    private lateinit var ima_back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_position_detail)

        Init()
    }

    private fun Init() {
        if (intent.extras != null) {
            orderId = intent.getStringExtra("orderId").toString()
        }

        txt_sd_strategyName = findViewById(R.id.txt_sd_strategyName)
        txt_detail_sd_description = findViewById(R.id.txt_detail_sd_description)
        txt_strategy_number = findViewById(R.id.txt_strategy_number)
        txt_strategy_quantity = findViewById(R.id.txt_strategy_quantity)
        txt_strategy_om = findViewById(R.id.txt_strategy_om)
        txt_strategy_symbol = findViewById(R.id.txt_strategy_symbol)
        txt_strategy_date = findViewById(R.id.txt_strategy_date)
        txt_strategy_tdt = findViewById(R.id.txt_strategy_tdt)
        txt_strategy_tt = findViewById(R.id.txt_strategy_tt)
        txt_buy_price = findViewById(R.id.txt_buy_price)
        txt_target_price = findViewById(R.id.txt_target_price)
        txt_sl_price = findViewById(R.id.txt_sl_price)
        txt_strategy_pl = findViewById(R.id.txt_strategy_pl)

        ima_back = findViewById(R.id.ima_back)
        ima_back.setOnClickListener(this)

        view = findViewById(R.id.btn_sell)
        register_progressBar = view.findViewById(R.id.register_progressBar)

        view2 = findViewById(R.id.btn_add_funds)
        register_progressBar2 = view2.findViewById(R.id.register_progressBar)

        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)
        resent.text = "Sell"

        register_progressBar2.visibility = View.GONE
        resent2 = view2.findViewById(R.id.resent)
        resent2.text = "Add Funds"


        resent.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(this@PositionDetailActivity, "Sell", Toast.LENGTH_SHORT).show()
            }
        })


        resent2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(this@PositionDetailActivity, "Add Funds", Toast.LENGTH_SHORT).show()
            }
        })

        executorService.submit {
            // handle WebSocket connection here
            //    scope.launch {

            client = OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build()

            job1 = CoroutineScope(Dispatchers.Main).launch {
                webSocket1 = createWebSocket("ws://103.14.99.42/LiveOrderDetailPagePL", 1)
                // ws://103.14.99.42/getStrategyPL
            }

            CoroutineScope(Dispatchers.IO).launch {
                job1.join() // Wait for job1 to complete before sending message to ws2
            }
            //}
        }

    }

    private fun createWebSocket(url: String, value: Int): WebSocket {
        //viewLoader.visibility = View.VISIBLE
        val request = Request.Builder().url(url).build()
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {

                if (value == 1) {
                    webSocket.send(orderId)
                }

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onMessage(webSocket: WebSocket, text: String) {

                if (value == 1) {
                    setPositionDetailActivity(text)
                }

            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                println("WebSocket disconnected from $url")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("WebSocket connection to $url failed: ${t.message}")
            }

        }

        return client.newWebSocket(request, listener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setPositionDetailActivity(text: String) {

        val gson = Gson()
        val positionDetailList = gson.fromJson(text, PositionDetailResponseItem::class.java)
        Constants.showLog("PDetailActivity", text)

        txt_sd_strategyName.text = positionDetailList.Strategy.StrategyName
        txt_detail_sd_description.text =
            positionDetailList.Strategy.Description
        txt_strategy_number.text =
            positionDetailList.Strategy.StrategyNumber.toString()
        txt_strategy_quantity.text = positionDetailList.Quantity.toString()
        txt_strategy_symbol.text = positionDetailList.Symbol
        txt_strategy_date.text = Constants.getDate(positionDetailList.Strategy.ModifiedDate)
        txt_strategy_tdt.text = Constants.getDate(positionDetailList.TradeDateTime)
        txt_buy_price.text = positionDetailList.BuyPrice.toString()
        txt_target_price.text = positionDetailList.Target.toString()
        txt_sl_price.text = positionDetailList.SL.toString()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (positionDetailList.OrderMode == 0) {
                txt_strategy_om.text = "Buying"
                txt_strategy_om.setTextColor(getColor(R.color.light_green))
            } else if (positionDetailList.OrderMode == 1) {
                txt_strategy_om.text = "Selling"
                txt_strategy_om.setTextColor(getColor(R.color.red))
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (positionDetailList.TradingType == 0) {
                txt_strategy_tt.text = "Auto"
            } else if (positionDetailList.OrderMode == 1) {
                txt_strategy_tt.text = "Manual"
            }
        }

        txt_strategy_pl.text = "SL" + positionDetailList.PL.toString()
        txt_strategy_pl.setTextColor(getColor(R.color.red))
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket1.close(1000, "Activity destroyed")
        job1.cancel() // Cancel the coroutine job when the activity is destroyed
//        scope.cancel()
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.ima_back -> {
                onBackPressed()
            }

        }
    }

}


//position_detail_adapter
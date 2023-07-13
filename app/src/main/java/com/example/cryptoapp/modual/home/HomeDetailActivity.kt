package com.example.cryptoapp.modual.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.StrategyDetailResponse
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class HomeDetailActivity : AppCompatActivity() {

    lateinit var txt_sd_strategyName: TextView
    lateinit var txt_sd_description: TextView
    lateinit var txt_sd_minCapital: TextView
    lateinit var txt_sd_monthlyFee: TextView
    lateinit var txt_sd_minCapital_price: TextView
    lateinit var txt_sd_monthlyFee_price: TextView
    lateinit var txt_strategy_pl: TextView

    lateinit var viewLoader: View
    lateinit var toolbar: View
    lateinit var toolbar_img_back: ImageView

    lateinit var animationView: LottieAnimationView

    lateinit var progressBar_cardView: RelativeLayout

    lateinit var webSocketListener: WebSocketListener
    lateinit var client: OkHttpClient
    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var job1: Job
    private val THREAD_POOL_SIZE = 10
    private val executorService: ExecutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE)
    lateinit var webSocket1: WebSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_detail)

        InIt()
    }

    private fun InIt() {
        txt_sd_strategyName = findViewById(R.id.txt_sd_strategyName)
        txt_sd_description = findViewById(R.id.txt_sd_description)
        txt_sd_minCapital = findViewById(R.id.txt_sd_minCapital)
        txt_sd_monthlyFee = findViewById(R.id.txt_sd_monthlyFee)
        txt_sd_minCapital_price = findViewById(R.id.txt_sd_minCapital_price)
        txt_sd_monthlyFee_price = findViewById(R.id.txt_sd_monthlyFee_price)
        txt_strategy_pl = findViewById(R.id.txt_strategy_pl)

        toolbar = findViewById(R.id.toolbar)
        toolbar_img_back = toolbar.findViewById(R.id.toolbar_img_back)

        viewLoader = findViewById(R.id.viewLoader)

        animationView = viewLoader.findViewById(R.id.lotti_img)
        animationView.visibility = View.GONE

        setupAnim()
        //  getStrategyId(intent.getStringExtra("strategyId").toString())

        toolbar_img_back.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
//                onBackPressed()
//                finish()
                openBottomSheet()
            }
        })

        txt_sd_minCapital_price.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                openBottomSheet()
            }
        })



        executorService.submit {
            // handle WebSocket connection here
            //    scope.launch {

            client = OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build()

            job1 = CoroutineScope(Dispatchers.Main).launch {
                webSocket1 = createWebSocket("ws://103.14.99.42/getStrategyDetail", 1)
               // ws://103.14.99.42/getStrategyPL
            }

            CoroutineScope(Dispatchers.IO).launch {
                job1.join() // Wait for job1 to complete before sending message to ws2
            }


            //}
        }

//        txt_sd_strategyName.text = "Strategy 1"
//        txt_sd_description.text = "Strategy Description : \n\n${getString(R.string.dummy_text)}"
//        txt_sd_minCapital_price.text = "10.00"
//        txt_sd_monthlyFee_price.text = "10.00"

    }

    private fun createWebSocket(url: String, value: Int): WebSocket {
        //viewLoader.visibility = View.VISIBLE
        val request = Request.Builder().url(url).build()
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {

                if (value == 1) {
                   //    webSocket.send(intent.getStringExtra("strategyId").toString())
                   // webSocket.send("54a0df6d-de7f-4c60-a868-1ec38b06f7ec")
                   webSocket.send("8799e47e-ceb3-46e6-8589-08db72efa6fe")

                }

            }

            override fun onMessage(webSocket: WebSocket, text: String) {

                if (value == 1) {
                    setGetStrategyByUser(text)
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

    private fun setGetStrategyByUser(text: String) {


        val gson = Gson()
        val objectList = gson.fromJson(text, StrategyDetailResponse::class.java)
        showLog("setGetStrategyByUser", objectList.toString())
        txt_sd_strategyName.text = "${objectList.Strategy.StrategyName}"
        txt_sd_description.text = "${objectList.Strategy.Description}"
        txt_sd_minCapital_price.text = objectList.Strategy.MinCapital.toString()
        txt_sd_monthlyFee_price.text = objectList.Strategy.MonthlyFee.toString()
        txt_strategy_pl.text = objectList.PL.toString()


    }


    private fun setupAnim() {
        animationView.setAnimation(R.raw.currency)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
//
        webSocket1.close(1000, "Activity destroyed")
        job1.cancel() // Cancel the coroutine job when the activity is destroyed
        scope.cancel()
        //cancel()
    }

    private fun openBottomSheet() {

        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.robot_bottom_sheet, null)
        dialog.setCancelable(true)


        dialog.setContentView(view)
        dialog.show()

    }
}
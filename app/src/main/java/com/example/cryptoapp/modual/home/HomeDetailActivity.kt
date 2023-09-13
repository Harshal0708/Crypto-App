package com.example.cryptoapp.modual.home

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.Constants
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.Response.DataXXXXX
import com.example.cryptoapp.modual.strategy.BuyCoinActivity
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class HomeDetailActivity : AppCompatActivity() {

    private lateinit var txt_sd_strategyName: TextView
    private lateinit var txt_sd_description: TextView
    private lateinit var txt_strategy_pl: TextView
    private lateinit var txt_sd_create_date: TextView
    private lateinit var txt_status_active: TextView
    private lateinit var txt_description: TextView

    private lateinit var viewLoader: View
    private lateinit var toolbar: View
    private lateinit var toolbar_img_back: ImageView

    private lateinit var view: View
    private lateinit var register_progressBar: ProgressBar

    private lateinit var animationView: LottieAnimationView

    private lateinit var progressBar_cardView: RelativeLayout
    private lateinit var client: OkHttpClient
    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var job1: Job
    private val THREAD_POOL_SIZE = 10
    private val executorService: ExecutorService =
        Executors.newFixedThreadPool(THREAD_POOL_SIZE)

    private lateinit var resent: TextView

    private lateinit var strategydata: DataXXXXX

    private lateinit var data: DataXX
    private lateinit var preferences: MyPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_detail)

        InIt()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun InIt() {

        preferences = MyPreferences(this)
        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)

        txt_sd_strategyName = findViewById(R.id.txt_sd_strategyName)
        txt_sd_description = findViewById(R.id.txt_sd_description)
        txt_sd_create_date = findViewById(R.id.txt_sd_create_date)
        txt_strategy_pl = findViewById(R.id.txt_strategy_pl)
        txt_status_active = findViewById(R.id.txt_status_active)
        txt_description = findViewById(R.id.txt_description)

        toolbar = findViewById(R.id.toolbar)
        toolbar_img_back = toolbar.findViewById(R.id.toolbar_img_back)

        viewLoader = findViewById(R.id.viewLoader)

        animationView = viewLoader.findViewById(R.id.lotti_img)
        animationView.visibility = View.GONE

        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)

        view.visibility = View.GONE
        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.next)

        setupAnim()

        toolbar_img_back.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                finish()
            }
        })

        resent.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                if(data.haveAnySubscription == false){
                    showToast(this@HomeDetailActivity,this@HomeDetailActivity,"Please subscribed...")
                }else{
                    openBottomSheet()
                }
            }
        })

        executorService.submit {

            client = OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build()

            var stringId = intent.getStringExtra("strategyId").toString()
            job1 = CoroutineScope(Dispatchers.Main).launch {
                if (!stringId.equals("")) {
                    getStrategyId(intent.getStringExtra("strategyId").toString())
                } else {
                    view.visibility = View.GONE
                    showToast(this@HomeDetailActivity,this@HomeDetailActivity, getString(R.string.something_wrong))
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                job1.join()
            }

        }
    }

    private fun setupAnim() {
        animationView.setAnimation(R.raw.currency)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        job1.cancel()
        scope.cancel()
    }

    private fun openBottomSheet() {

        val dialog = BottomSheetDialog(this)
        val viewBottom = layoutInflater.inflate(R.layout.robot_bottom_sheet, null)
        dialog.setCancelable(true)

        var auto = false
        var manual = false

        val btn_progressBar: TextView = viewBottom.findViewById(R.id.btn_progressBar)

        val con_auto_coin: ConstraintLayout = viewBottom.findViewById(R.id.con_auto_coin)
        val con_manual_coin: ConstraintLayout = viewBottom.findViewById(R.id.con_manual_coin)


        con_auto_coin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                auto = true
                manual = false
                con_auto_coin.setBackground(
                    ContextCompat.getDrawable(
                        this@HomeDetailActivity,
                        R.drawable.coin_select_background
                    )
                )
                con_manual_coin.setBackground(
                    ContextCompat.getDrawable(
                        this@HomeDetailActivity,
                        R.drawable.coin_background
                    )
                )
            }
        })

        con_manual_coin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                auto = false
                manual = true
                con_manual_coin.setBackground(
                    ContextCompat.getDrawable(
                        this@HomeDetailActivity,
                        R.drawable.coin_select_background
                    )
                )
                con_auto_coin.setBackground(
                    ContextCompat.getDrawable(
                        this@HomeDetailActivity,
                        R.drawable.coin_background
                    )
                )
            }
        })


        btn_progressBar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                var coin = -1
                if (auto == true) {
                    coin = 0
                } else if (manual == true) {
                    coin = 1
                } else {
                    coin = -1
                }

                if (coin == -1) {
                    showToast(
                        this@HomeDetailActivity,
                        this@HomeDetailActivity,
                        this@HomeDetailActivity.getString(R.string.Please_select_option)
                    )
                } else {
                    val intent = Intent(this@HomeDetailActivity, BuyCoinActivity::class.java)
                    intent.putExtra("tradingType", coin)
                    intent.putExtra("strategyId", strategydata.id)
                    intent.putExtra("userId", data.userId)
                    startActivity(intent)
                    dialog.dismiss()
                }

            }
        })

        dialog.setContentView(viewBottom)
        dialog.show()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getStrategyId(id: String) {
        animationView.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder(this@HomeDetailActivity,false).buildService(RestApi::class.java)
                .getStrategyById(id)
            withContext(Dispatchers.Main) {

                if(response.body()!!.isSuccess){
                    animationView.visibility = View.GONE
                    view.visibility = View.VISIBLE

                    strategydata = response.body()!!.data
                    txt_sd_strategyName.text = strategydata.strategyName
                    txt_description.text = getString(R.string.strategy_description)
                    txt_sd_description.text = strategydata.description
                    txt_sd_create_date.text = Constants.getDate(strategydata.createdDate)

                    if (response.body()!!.data.isActive != true) {
                        txt_status_active.text = resources.getString(R.string.not_active)
                        txt_status_active.setTextColor(
                            ContextCompat.getColor(
                                this@HomeDetailActivity,
                                R.color.red
                            )
                        )
                    } else {
                        txt_status_active.text = resources.getString(R.string.active)
                        txt_status_active.setTextColor(
                            ContextCompat.getColor(
                                this@HomeDetailActivity,
                                R.color.light_green
                            )
                        )
                    }
                }else{
                    showToast(this@HomeDetailActivity,this@HomeDetailActivity, getString(R.string.data_not_found))
                }

            }
        }
    }
}

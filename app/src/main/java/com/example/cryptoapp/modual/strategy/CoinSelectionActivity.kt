package com.example.cryptoapp.modual.strategy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.modual.strategy.adapter.BuyCoinAdapter
import com.example.cryptoapp.modual.strategy.adapter.CoinSelectionAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CoinSelectionActivity : AppCompatActivity() {

    var list = ArrayList<CoinDataset>()
    lateinit var  adapter: CoinSelectionAdapter
    lateinit var rvSelection : RecyclerView
    lateinit var btn_progressBar:TextView

    var coin_selection: String = ""
    var sliderPrice: Int = -1
    var tradingType: Int = -1
    var strategyId: String = ""
    var userId: String = ""

    private lateinit var data: DataXX
    private lateinit var preferences: MyPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_selection)

        preferences = MyPreferences(this)
        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)
        rvSelection = findViewById(R.id.rvSelection)
        btn_progressBar = findViewById(R.id.btn_progressBar)

        coin_selection =  intent.extras?.getString("coin_selection").toString()
        sliderPrice =  intent.extras!!.getInt("slider_price",-1)
        tradingType = intent.getIntExtra("tradingType",-1)
        strategyId = intent.getStringExtra("strategyId").toString()
        userId = intent.getStringExtra("userId").toString()

        getLiveTopGainers()
    }

    private fun getLiveTopGainers() {
        // viewLoader.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder(this@CoinSelectionActivity,false).buildService(RestApi::class.java)
                .getLiveTopGainers(data.userId)
            withContext(Dispatchers.Main) {
                //viewLoader.visibility = View.GONE
                Constants.showLog("getLiveTopGainers",Gson().toJson(response.body()))

                adapter = CoinSelectionAdapter(this@CoinSelectionActivity,response.body()!!)
                rvSelection.layoutManager = LinearLayoutManager(this@CoinSelectionActivity)
                rvSelection.adapter=adapter

                btn_progressBar.setOnClickListener {
                    if (adapter.customMethod(
                            coin_selection,
                            sliderPrice,
                            tradingType,
                            strategyId,
                            userId
                        )
                    ) {
                        this@CoinSelectionActivity.finish()
                    } else {
                        Constants.showToast(this@CoinSelectionActivity, this@CoinSelectionActivity,resources.getString(R.string.Please_other_select_coin))
                    }

                }

            }
        }
    }
}
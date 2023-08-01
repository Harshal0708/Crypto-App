package com.example.cryptoapp.modual.strategy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.home.adapter.CryptoName
import com.example.cryptoapp.modual.strategy.adapter.CoinSelectionAdapter

class CoinSelectionActivity : AppCompatActivity() {

    var list = ArrayList<CoinDataset>()
    lateinit var  adapter: CoinSelectionAdapter
    lateinit var rv_selection_coin : RecyclerView
    lateinit var first: ArrayList<CryptoName>
    lateinit var btn_progressBar:TextView

    var coin_selection: String = ""
    var slider_price: Int = -1
    var tradingType: Int = -1
    var strategyId: String = ""
    var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_selection)

        rv_selection_coin = findViewById(R.id.rv_selection_coin)
        btn_progressBar = findViewById(R.id.btn_progressBar)

        coin_selection =  intent.extras?.getString("coin_selection").toString()
        slider_price =  intent.extras!!.getInt("slider_price",-1)
        tradingType = intent.getIntExtra("tradingType",-1)
        strategyId = intent.getStringExtra("strategyId").toString()
        userId = intent.getStringExtra("userId").toString()

        Constants.showLog("coin",coin_selection)
        Constants.showLog("slider_price",slider_price.toString())
        Constants.showLog("tradingType",tradingType.toString())
        Constants.showLog("strategyId",strategyId)
        Constants.showLog("userId",userId)

        list.add(CoinDataset("BTCUSDT"))
        list.add(CoinDataset("MAGICUSDT"))
        list.add(CoinDataset("ETHUSDT"))
        list.add(CoinDataset("MCUSDT"))
        list.add(CoinDataset("BNBUSDT"))
        list.add(CoinDataset("NEOUSDT"))
        list.add(CoinDataset("LTCUSDT"))
        list.add(CoinDataset("EOSUSDT"))

        adapter = CoinSelectionAdapter(this@CoinSelectionActivity,list)
        rv_selection_coin.layoutManager = LinearLayoutManager(this)
        rv_selection_coin.adapter=adapter

        btn_progressBar.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
               if(adapter.customMethod(coin_selection,slider_price,tradingType,strategyId,userId)==true){
                   this@CoinSelectionActivity.finish()
               }else{
                   Constants.showToast(this@CoinSelectionActivity,"Please select a coin")
               }
            }
        })

    }
}
package com.example.cryptoapp.modual.strategy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.strategy.adapter.CoinSelectionAdapter

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_selection)

        rvSelection = findViewById(R.id.rvSelection)
        btn_progressBar = findViewById(R.id.btn_progressBar)

        coin_selection =  intent.extras?.getString("coin_selection").toString()
        sliderPrice =  intent.extras!!.getInt("slider_price",-1)
        tradingType = intent.getIntExtra("tradingType",-1)
        strategyId = intent.getStringExtra("strategyId").toString()
        userId = intent.getStringExtra("userId").toString()

        list.add(CoinDataset("BTCUSDT"))
        list.add(CoinDataset("MAGICUSDT"))
        list.add(CoinDataset("ETHUSDT"))
        list.add(CoinDataset("MCUSDT"))
        list.add(CoinDataset("BNBUSDT"))
        list.add(CoinDataset("NEOUSDT"))
        list.add(CoinDataset("LTCUSDT"))
        list.add(CoinDataset("EOSUSDT"))

        adapter = CoinSelectionAdapter(this@CoinSelectionActivity,list)
        rvSelection.layoutManager = LinearLayoutManager(this)
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
                Constants.showToast(this@CoinSelectionActivity, resources.getString(R.string.Please_other_select_coin))
            }
        }

    }
}
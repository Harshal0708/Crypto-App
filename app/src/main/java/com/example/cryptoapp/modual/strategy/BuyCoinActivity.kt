package com.example.cryptoapp.modual.strategy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.home.adapter.TopCoins
import com.example.cryptoapp.modual.strategy.adapter.BuyCoinAdapter
import com.example.cryptoapp.modual.strategy.adapter.BuyCoins

class BuyCoinActivity : AppCompatActivity() {

    lateinit var rv_buy_coin : RecyclerView
    lateinit var buyCoins : ArrayList<BuyCoins>
    lateinit var buyCoinAdapter: BuyCoinAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_coin)

        init()
    }

    fun init(){
        rv_buy_coin = findViewById(R.id.rv_buy_coin)
        buyCoins = ArrayList()

        buyCoins.add(BuyCoins("", "100 $", "500 $", "5 Coins"))
        buyCoins.add(BuyCoins("", "500 $", "1000 $", "10 Coins"))
        buyCoins.add(BuyCoins("", "1000 $", "Unlimited", "15 Coins"))

        rv_buy_coin.layoutManager = LinearLayoutManager(this)
        buyCoinAdapter = BuyCoinAdapter(this, buyCoins)
        rv_buy_coin.adapter = buyCoinAdapter

    }
}
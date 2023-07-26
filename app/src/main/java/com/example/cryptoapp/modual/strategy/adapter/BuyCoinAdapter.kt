package com.example.cryptoapp.modual.strategy.adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXXXX
import com.example.cryptoapp.modual.strategy.BuyCoinActivity
import com.example.cryptoapp.modual.strategy.CoinSelectionActivity
import com.example.cryptoapp.modual.strategy.WelcomeActivity
import com.example.cryptoapp.network.onItemClickListener

class BuyCoinAdapter(
    var context: Context,
    var buyCoinsList: List<DataXXXX>,
    val onItemClickListener: onItemClickListener,
    var activity: Activity,
) :
    RecyclerView.Adapter<BuyCoinAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var txt_range_price: TextView = itemView.findViewById(R.id.txt_range_price)
        var txt_coin: TextView = itemView.findViewById(R.id.txt_coin)
        var btn_buy: TextView = itemView.findViewById(R.id.btn_buy)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.buy_coin_adapter, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_range_price.text =
            "$" + buyCoinsList.get(position).startValue.toString() + " - " + "$" + buyCoinsList.get(
                position
            ).endValue.toString()
        holder.txt_coin.text = buyCoinsList.get(position).coinCount.toString() + " Coin"

        holder.btn_buy.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }


    }

    override fun getItemCount(): Int {
        return buyCoinsList.size
    }

}
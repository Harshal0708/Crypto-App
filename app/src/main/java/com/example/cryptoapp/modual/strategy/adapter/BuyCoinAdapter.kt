package com.example.cryptoapp.modual.strategy.adapter

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
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.strategy.BuyCoinActivity
import com.example.cryptoapp.modual.strategy.WelcomeActivity

class BuyCoinAdapter (
    var context: Context,
    var buyCoinsList: List<BuyCoins>
) :
    RecyclerView.Adapter<BuyCoinAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var txt_range_price: TextView = itemView.findViewById(R.id.txt_range_price)
        var txt_coin: TextView = itemView.findViewById(R.id.txt_coin)
        var btn_buy: Button = itemView.findViewById(R.id.btn_buy)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.buy_coin_adapter, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_range_price.text = buyCoinsList.get(position).coin_start + " - " + buyCoinsList.get(position).coin_end
        holder.txt_coin.text = buyCoinsList.get(position).coin


        holder.btn_buy.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(context, WelcomeActivity::class.java)
                context.startActivity(intent)
            }
        })


    }

    override fun getItemCount(): Int {
        return buyCoinsList.size
    }

}
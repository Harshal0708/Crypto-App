package com.example.cryptoapp.modual.watchlist.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.StrategyRes
import com.example.cryptoapp.Response.TickerResponseItem
import com.example.cryptoapp.modual.home.adapter.AirQualityData

class WatchlistAdapter (var context: Context, var tickerResponseItem:  ArrayList<AirQualityData>) :
    RecyclerView.Adapter<WatchlistAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
       var output_name: TextView = itemView.findViewById(R.id.output_name)
       var output_price: TextView = itemView.findViewById(R.id.output_price)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.ticker_respons_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder.output_name.text = "${tickerResponseItem?.get(position)?.name}"
            holder.output_price.text = "${tickerResponseItem?.get(position)?.price}"


    }

    override fun getItemCount(): Int {
        return tickerResponseItem.size
    }


}
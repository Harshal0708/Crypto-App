package com.example.cryptoapp.modual.watchlist.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.StrategyRes
import com.example.cryptoapp.Response.TickerResponseItem
import com.example.cryptoapp.modual.home.adapter.AirQualityData
import com.squareup.picasso.Picasso

class WatchlistAdapter(var context: Context, var tickerResponseItem: ArrayList<AirQualityData>) :
    RecyclerView.Adapter<WatchlistAdapter.ViewHolder>() {

    var previousPrice = 0.0

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

//        val currentPrice = tickerResponseItem?.get(position)?.price!!.toDouble()
//
//        // Determine the color
//        val color = when {
//            currentPrice > previousPrice -> "Green"
//            currentPrice < previousPrice -> "Red"
//            else -> "No change"
//        }


//        val color = if (currentPrice >= 0) "green" else "red"



//        val color = if (currentPrice > previousPrice) "green" else "red"
//
//        if (color.equals("red")) {
//            holder.output_price.setTextColor(context.resources.getColor(R.color.red))
//        } else if (color.equals("green")) {
//            holder.output_price.setTextColor(context.resources.getColor(R.color.light_green))
//        }

//        holder.output_price.text = "$ ${currentPrice}"

//        holder.output_price.apply {
//            text = color
//            setTextColor(tickerResponseItem.get(position).priceChangeColor)
//        }

        holder.output_name.text = "${tickerResponseItem?.get(position)?.name}"
        holder.output_price.text = "$ ${tickerResponseItem?.get(position)?.price!!}"

        //previousPrice = currentPrice

    }

    override fun getItemCount(): Int {
        return tickerResponseItem.size
    }

}
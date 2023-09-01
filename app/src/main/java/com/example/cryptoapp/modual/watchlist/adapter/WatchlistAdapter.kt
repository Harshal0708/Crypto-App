package com.example.cryptoapp.modual.watchlist.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.LiveTopGainersResponseItem
import com.example.cryptoapp.Response.StrategyRes
import com.example.cryptoapp.Response.TickerResponseItem
import com.example.cryptoapp.modual.home.adapter.AirQualityData
import com.example.cryptoapp.singleton.MySingleton
import com.squareup.picasso.Picasso

class WatchlistAdapter(var context: Context, var tickerResponseItem: ArrayList<LiveTopGainersResponseItem>) :
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

        holder.output_name.text = "${tickerResponseItem.get(position).Symbol}"
        holder.output_name.setTextColor(ContextCompat.getColor(context, R.color.black))
        holder.output_price.text = "$ ${tickerResponseItem.get(position).Price}"
//        if (tickerResponseItem.get(position).Price.toDouble() > tickerResponseItem.get(position).prePrice.toDouble()) {
//            holder.output_price.setTextColor(ContextCompat.getColor(context, R.color.light_green))
//        } else {

//            holder.output_price.setTextColor(ContextCompat.getColor(context, R.color.red))
//        }
//
//        holder.output_price.text = "$ ${tickerResponseItem.get(position).prePrice}"


//        holder.output_name.text = "${tickerResponseItem.get(position).name}"
//        holder.output_name.setTextColor(ContextCompat.getColor(context, R.color.black))

//        var previousPrice = MySingleton().getStoredValue()
//        var previousPrice = MySingleton().getTickerResponseItem()

//            Constants.showLog("previousPrice", previousPrice.price.toDouble().toString())
//            Constants.showLog("------", "-------------------------------------")

        //  val currentPrice = tickerResponseItem.get(position)?.price!!.toDouble()

//        if (!previousPrice.price.equals("")) {

//            if(tickerResponseItem.get(position).name.equals(previousPrice.name)){
//
//            }

//            val color =
//                if (tickerResponseItem.get(position).price.toDouble() > previousPrice.price.toDouble()) "green" else "red"
//            val value =
//                tickerResponseItem.get(position).price.toDouble() > previousPrice.price.toDouble()
//
//            if (color.equals("red")) {
//                holder.output_price.setTextColor(ContextCompat.getColor(context, R.color.red))
//                holder.output_price.text = "$ ${tickerResponseItem.get(position).price}"
//            } else if (color.equals("green")) {
//                holder.output_price.setTextColor(
//                    ContextCompat.getColor(
//                        context,
//                        R.color.light_green
//                    )
//                )
//                holder.output_price.text = "$ ${tickerResponseItem.get(position).price}"
//            } else {
//                holder.output_price.setTextColor(
//                    ContextCompat.getColor(
//                        context,
//                        R.color.black_color
//                    )
//                )
//                holder.output_price.text = "$ ${tickerResponseItem.get(position).price}"
//            }
//
//        }else{
//            holder.output_price.setTextColor(
//                ContextCompat.getColor(
//                    context,
//                    R.color.black_color
//                )
//            )
//            holder.output_price.text = "$ ${tickerResponseItem.get(position).price}"
//        }

//        holder.output_price.text = "$ ${tickerResponseItem.get(position).price}"
//        holder.output_name.text = "${tickerResponseItem.get(position).name}"
//        holder.output_name.setTextColor(ContextCompat.getColor(context, R.color.black))
//        MySingleton().setTickerResponseItem(tickerResponseItem.get(position))


//        MySingleton().setStoredValue(tickerResponseItem.get(position).price.toDouble())


    }

    override fun getItemCount(): Int {
        return tickerResponseItem.size
    }

}

//        holder.output_price.text = "$ ${currentPrice}"

//        holder.output_price.apply {
//            text = color
//            setTextColor(tickerResponseItem.get(position).priceChangeColor)
//        }
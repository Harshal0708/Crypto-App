package com.example.cryptoapp.modual.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.LiveOrderResponseItem

class PositionAdapter(
    var context: Context,
    val liveOrderResponseItem: ArrayList<LiveOrderResponseItem>
) : RecyclerView.Adapter<PositionAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txt_position_coin: TextView = itemView.findViewById(R.id.txt_position_coin)
        var txt_position_pl: TextView = itemView.findViewById(R.id.txt_position_pl)
        var txt_position_num: TextView = itemView.findViewById(R.id.txt_position_num)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.live_position_adapter, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        Constants.showLog("test", liveOrderResponseItem.get(0).toString())

        holder.txt_position_coin.text =liveOrderResponseItem.get(position).Strategy.StrategyName
        holder.txt_position_pl.text =liveOrderResponseItem.get(position).PL.toString()
        holder.txt_position_num.text =liveOrderResponseItem.get(position).Strategy.Description

    }


    override fun getItemCount(): Int {
        return liveOrderResponseItem.size
    }
}
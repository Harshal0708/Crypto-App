package com.example.cryptoapp.modual.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R

class TopCoinAdapter (var context: Context, var topCoins: ArrayList<TopCoins>) :
    RecyclerView.Adapter<TopCoinAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var txt_coin_name: TextView = itemView.findViewById(R.id.txt_coin_name)
        var txt_coin_sub_name: TextView = itemView.findViewById(R.id.txt_coin_sub_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.top_coin_adapter, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.txt_coin_name.text = "Apurva"
//        holder.txt_coin_sub_name.text = "patel"
//
        holder.txt_coin_name.text = topCoins.get(position).coin_name
        holder.txt_coin_sub_name.text = topCoins.get(position).coin_sub_name


    }

    override fun getItemCount(): Int {
        return topCoins.size
    }

}
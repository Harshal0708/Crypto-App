package com.example.cryptoapp.modual.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.OrderHistory

class TraderHistoryAdapter(var context: Context, val orderHistories: List<OrderHistory>) : RecyclerView.Adapter<TraderHistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        //var txt_order_history_name : TextView = itemView.findViewById(R.id.txt_order_history_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.trade_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     //   holder.txt_order_history_name.text=orderHistories.get(position).userId

    }

    override fun getItemCount(): Int {
        return orderHistories.size
    }

}
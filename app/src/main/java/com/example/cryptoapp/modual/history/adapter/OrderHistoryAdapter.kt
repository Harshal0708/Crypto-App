package com.example.cryptoapp.modual.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.OrderHistory

class OrderHistoryAdapter(var context: Context,  val orderHistories: List<OrderHistory>) : RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var txt_order_history_name : TextView = itemView.findViewById(R.id.txt_order_history_name)
        var txt_order_history_quantity : TextView = itemView.findViewById(R.id.txt_order_history_quantity)
        var txt_order_history_price : TextView = itemView.findViewById(R.id.txt_order_history_price)
        var txt_order_history_status : TextView = itemView.findViewById(R.id.txt_order_history_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.order_history_adapter,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_order_history_name.text=orderHistories.get(position).userId
        holder.txt_order_history_quantity.text="Quantity :- ${orderHistories.get(position).quantity}"
        holder.txt_order_history_price.text="Price :- ${orderHistories.get(position).price}"
        holder.txt_order_history_status.text="Status :- ${orderHistories.get(position).status}"
    }

    override fun getItemCount(): Int {
        return orderHistories.size
    }

}
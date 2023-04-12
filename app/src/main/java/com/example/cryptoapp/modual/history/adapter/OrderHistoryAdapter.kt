package com.example.cryptoapp.modual.history.adapter

import android.content.Context
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.OrderHistory
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class OrderHistoryAdapter(var context: Context, val orderHistories: ArrayList<OrderHistory>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_DATA = 0
        private const val TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DATA -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.order_history_adapter, parent, false)
                ViewHolder(view)
            }
            TYPE_PROGRESS -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.progressbar, parent, false)
                ProgressbarViewHolder(view)
            }
            else -> throw java.lang.IllegalArgumentException("Different view type")

        }
    }
    fun addLoadingView() {
        //add loading item
        Handler().post {
           // orderHistories.add(null)
            orderHistories.clear()
            notifyItemInserted(orderHistories.size - 1)
        }
    }
    fun removeLoadingView() {
        //Remove loading item
        if (orderHistories.size != 0) {
            orderHistories.removeAt(orderHistories.size - 1)
            notifyItemRemoved(orderHistories.size)
        }
    }

    inner class ProgressbarViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txt_order_history_name: TextView = itemView.findViewById(R.id.txt_order_history_name)
        var txt_order_history_quantity: TextView =
            itemView.findViewById(R.id.txt_order_history_quantity)
        var txt_order_history_price: TextView = itemView.findViewById(R.id.txt_order_history_price)
        var txt_order_history_status: TextView =
            itemView.findViewById(R.id.txt_order_history_status)
        var txt_order_history_trading_type: TextView =
            itemView.findViewById(R.id.txt_order_history_trading_type)
        var txt_order_history_trading_date: TextView =
            itemView.findViewById(R.id.txt_order_history_trading_date)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHolder) {

            holder.txt_order_history_name.text = orderHistories.get(position)?.symbol
//        holder.txt_order_history_quantity.text="Quantity :- ${orderHistories.get(position).quantity}"
//        holder.txt_order_history_price.text="Price :- ${orderHistories.get(position).price}"
//        holder.txt_order_history_status.text="Status :- ${orderHistories.get(position).status}"

            holder.txt_order_history_quantity.text = "${orderHistories.get(position)?.quantity}"
            holder.txt_order_history_price.text = "${orderHistories.get(position)?.price}"

            val dateString = orderHistories.get(position)?.timestamp
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = format.parse(dateString)
            holder.txt_order_history_trading_date.text = "${date}"

            if (orderHistories.get(position)?.side == 0) {
                holder.txt_order_history_status.text = "Buy"
            } else if (orderHistories.get(position)?.side == 1) {
                holder.txt_order_history_status.text = "Sell"
            }

//        if(orderHistories.get(position).tradingType == 0){
//            holder.txt_order_history_trading_type.text="Auto"
//        }else if(orderHistories.get(position).type == 1){
//            holder.txt_order_history_trading_type.text="Manual"
//        }

            holder.txt_order_history_trading_type.text = "Market"
        }

    }

    //    override fun getItemViewType(position: Int): Int {
//        var viewType =orderHistories.get(position).ca
//
//        return when (viewType){
//            "data"-> TYPE_DATA
//            else -> TYPE_PROGRESS
//        }
//    }

    override fun getItemCount(): Int {
        return orderHistories.size
    }
}
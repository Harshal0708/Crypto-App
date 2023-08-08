package com.example.cryptoapp.modual.history.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.OrderHistoryDetailResponseItemX

class StrategyHistoryDetailAdapter(var context: Context, val orderHistoryDetailResponseItem:  List<OrderHistoryDetailResponseItemX>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_DATA = 0
        private const val TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DATA -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.strategy_history_detail_adapter, parent, false)
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
//    fun addLoadingView() {
//        //add loading item
//        Handler().post {
//            // orderHistories.add(null)
//            orderHistoryDetailResponseItem.clear()
//            notifyItemInserted(orderHistoryDetailResponseItem.size - 1)
//        }
//    }
//    fun removeLoadingView() {
//        //Remove loading item
//        if (orderHistoryDetailResponseItem.size != 0) {
//            orderHistoryDetailResponseItem.removeAt(orderHistoryDetailResponseItem.size - 1)
//            notifyItemRemoved(orderHistoryDetailResponseItem.size)
//        }
//    }

    inner class ProgressbarViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txt_order_history_name: TextView = itemView.findViewById(R.id.txt_order_history_name)
        var txt_order_hq_price: TextView = itemView.findViewById(R.id.txt_order_hq_price)
        var txt_order_bp_price: TextView = itemView.findViewById(R.id.txt_order_bp_price)
        var txt_order_sp_price: TextView = itemView.findViewById(R.id.txt_order_sp_price)
        var txt_strategy_number: TextView = itemView.findViewById(R.id.txt_strategy_number)
        var txt_strategy_pl: TextView = itemView.findViewById(R.id.txt_strategy_pl)
        var txt_order_et_time: TextView = itemView.findViewById(R.id.txt_order_et_time)
        var txt_order_tt_price: TextView = itemView.findViewById(R.id.txt_order_tt_price)
        var txt_order_end_time_price: TextView = itemView.findViewById(R.id.txt_order_end_time_price)
        var txt_order_mode_price: TextView = itemView.findViewById(R.id.txt_order_mode_price)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHolder) {
            holder.txt_order_history_name.text = orderHistoryDetailResponseItem.get(position).symbol
            holder.txt_order_hq_price.text = orderHistoryDetailResponseItem.get(position).quantity.toString()
            holder.txt_order_bp_price.text = orderHistoryDetailResponseItem.get(position).buyPrice.toString()
            holder.txt_order_sp_price.text = orderHistoryDetailResponseItem.get(position).sellPrice.toString()
            holder.txt_strategy_number.text = orderHistoryDetailResponseItem.get(position).strategy.strategyNumber.toString()
            holder.txt_strategy_pl.text =orderHistoryDetailResponseItem.get(position).pl.toString()
            holder.txt_strategy_pl.setTextColor(context.getColor(R.color.red))

            holder.txt_order_et_time.text = Constants.getDate(orderHistoryDetailResponseItem.get(position).tradeEntryTime)
            if(orderHistoryDetailResponseItem.get(position).tradingType == 0){
                holder.txt_order_tt_price.text = "AUTO"
            }else{
                holder.txt_order_tt_price.text = "Manual"
            }

            holder.txt_order_end_time_price.text = Constants.getDate(orderHistoryDetailResponseItem.get(position).tradeExitTime)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (orderHistoryDetailResponseItem.get(position).orderMode == 0) {
                    holder.txt_order_mode_price.text = "Buying"
                    holder.txt_order_mode_price.setTextColor(context.getColor(R.color.light_green))
                } else if (orderHistoryDetailResponseItem.get(position).orderMode == 1) {
                    holder.txt_order_mode_price.text = "Selling"
                    holder.txt_order_mode_price.setTextColor(context.getColor(R.color.red))
                }
            }

        }

    }


    override fun getItemCount(): Int {
        return orderHistoryDetailResponseItem.size
    }
}
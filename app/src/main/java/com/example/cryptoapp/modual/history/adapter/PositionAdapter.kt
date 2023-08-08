package com.example.cryptoapp.modual.history.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.LiveOrderResponseModel
import com.example.cryptoapp.modual.history.PositionDetailActivity

class PositionAdapter(
    var context: Context,
    val liveOrderResponseItem: List<LiveOrderResponseModel>
) : RecyclerView.Adapter<PositionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txt_position_coin: TextView = itemView.findViewById(R.id.txt_position_coin)
        var txt_position_pl_price: TextView = itemView.findViewById(R.id.txt_position_pl_price)
        var txt_position_num: TextView = itemView.findViewById(R.id.txt_position_num)
        var txt_position_buy_price: TextView = itemView.findViewById(R.id.txt_position_buy_price)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.live_position_adapter, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        Constants.showLog("test", liveOrderResponseItem.get(0).toString())

        holder.txt_position_coin.text =liveOrderResponseItem.get(position).Strategy.StrategyName
        holder.txt_position_pl_price.text =liveOrderResponseItem.get(position).PL.toString()
        holder.txt_position_pl_price.setTextColor(context.getColor(R.color.red))
        holder.txt_position_num.text =liveOrderResponseItem.get(position).Strategy.Description
        holder.txt_position_buy_price.text =liveOrderResponseItem.get(position).BuyPrice.toString()



        holder.itemView.setOnClickListener {
            val intent = Intent(context, PositionDetailActivity::class.java)
            intent.putExtra("orderId", liveOrderResponseItem.get(position).OrderId)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return liveOrderResponseItem.size
    }
}


package com.example.cryptoapp.modual.subscription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.subscription.SubscriptionHistoryModel

class SubHistoryAdapter(var context: Context , var subscriptionHistoryModel: ArrayList<SubscriptionHistoryModel>) : RecyclerView.Adapter<SubHistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var txt_sub_history_name : TextView = itemView.findViewById(R.id.txt_sub_history_name)
        var txt_sub_startdate : TextView = itemView.findViewById(R.id.txt_sub_startdate)
        var txt_sub_enddate : TextView = itemView.findViewById(R.id.txt_sub_enddate)
        var txt_sub_price : TextView = itemView.findViewById(R.id.txt_sub_price)
        var txt_sub_status : TextView = itemView.findViewById(R.id.txt_sub_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.sub_history_adapter,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.txt_sub_history_name.text=subscriptionHistoryModel.get(position).name
       holder.txt_sub_startdate.text=subscriptionHistoryModel.get(position).startDate
       holder.txt_sub_enddate.text=subscriptionHistoryModel.get(position).endDate
       holder.txt_sub_price.text=subscriptionHistoryModel.get(position).price.toString()
        holder.txt_sub_status.text=subscriptionHistoryModel.get(position).status
        if(subscriptionHistoryModel.get(position).status.equals("Purchased")){
            holder.txt_sub_status.setTextColor(context.resources.getColor(R.color.light_green))
        }else{

            holder.txt_sub_status.setTextColor(context.resources.getColor(R.color.red))
        }

    }

    override fun getItemCount(): Int {
        return subscriptionHistoryModel.size
    }

}
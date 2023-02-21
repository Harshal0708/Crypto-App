package com.example.cryptoapp.modual.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.UserSubscription

class SubscriptionHistoryAdapter(var context: Context,val userSubscriptions: List<UserSubscription>) : RecyclerView.Adapter<SubscriptionHistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var txt_sub_history_name : TextView = itemView.findViewById(R.id.txt_sub_history_name)
        var txt_sub_history_subscription_date : TextView = itemView.findViewById(R.id.txt_sub_history_subscription_date)
        var txt_sub_history_subscription_exp_date : TextView = itemView.findViewById(R.id.txt_sub_history_subscription_exp_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.subscription_history_adapter,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_sub_history_name.text=userSubscriptions.get(position).userId
        holder.txt_sub_history_subscription_date.text="Start Date :- ${userSubscriptions.get(position).subscriptionDate}"
        holder.txt_sub_history_subscription_exp_date.text="Expiry Date :- ${userSubscriptions.get(position).subscriptionExpDate}"
    }

    override fun getItemCount(): Int {
        return userSubscriptions.size
    }

}
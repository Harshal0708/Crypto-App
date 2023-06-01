package com.example.cryptoapp.modual.history.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants.Companion.getDate
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.UserSubscription
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

class SubscriptionHistoryAdapter(var context: Context,val userSubscriptions: List<UserSubscription>) : RecyclerView.Adapter<SubscriptionHistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var txt_sub_history_name : TextView = itemView.findViewById(R.id.txt_sub_history_name)
        var txt_sub_history_subscription_date : TextView = itemView.findViewById(R.id.txt_sub_history_subscription_date)
        var txt_sub_history_subscription_exp_date : TextView = itemView.findViewById(R.id.txt_sub_history_subscription_exp_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.subscription_history_adapter,parent,false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val datetimeString = euserSubscriptions.get(position).subscriptionDat // Example datetime string, adjust it to match your input
//        val parsedDateTime = parseDateTime(datetimeString)
//        if (parsedDateTime != null) {
//            println("Parsed datetime: $parsedDateTime")
//            // Perform further operations with the parsed datetime
//        }

//        val subscriptionDate =userSubscriptions.get(position).subscriptionDate
//        val subscriptionExpDate =userSubscriptions.get(position).subscriptionExpDate



        holder.txt_sub_history_name.text=userSubscriptions.get(position).userId
//        holder.txt_sub_history_subscription_date.text="Start Date :- ${userSubscriptions.get(position).subscriptionDate}"
        holder.txt_sub_history_subscription_date.text="Start Date :- ${getDate(userSubscriptions.get(position).subscriptionDate)}"
        holder.txt_sub_history_subscription_exp_date.text="Expiry Date :- ${getDate(userSubscriptions.get(position).subscriptionExpDate)}"
    }

    override fun getItemCount(): Int {
        return userSubscriptions.size
    }
}
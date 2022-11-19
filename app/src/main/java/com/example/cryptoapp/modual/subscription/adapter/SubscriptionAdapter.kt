package com.example.cryptoapp.modual.subscription.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.UserSubscriptionItem
import com.example.cryptoapp.modual.login.fragment.ScriptFragment
import com.example.cryptoapp.modual.subscription.SubscriptionDetailActivity
import com.example.cryptoapp.modual.subscription.SubscriptionModel

class SubscriptionAdapter (val context: Context, var subscriptionModelList: ArrayList<UserSubscriptionItem>,var subscriptionName:String,var subscriptionPrice:String,var  noOfStrategies:String) :
    RecyclerView.Adapter<SubscriptionAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txt_subscriptionName : TextView = itemView.findViewById(R.id.txt_subscriptionName)
        var txt_subscriptionPrice : TextView = itemView.findViewById(R.id.txt_subscriptionPrice)
        var txt_subscriptionMoreDetail : TextView = itemView.findViewById(R.id.txt_subscriptionMoreDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(context).inflate(R.layout.subscription_adapter,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_subscriptionName.text=subscriptionModelList.get(position).subscriptionName
        holder.txt_subscriptionPrice.text="$  ${subscriptionModelList.get(position).subscriptionPrice}"
        holder.txt_subscriptionMoreDetail.setOnClickListener {
            val intent  = Intent(context,SubscriptionDetailActivity::class.java)
            intent.putExtra("subscriptionName", subscriptionName)
            intent.putExtra("subscriptionPrice", subscriptionPrice)
            intent.putExtra("noOfStrategies", noOfStrategies)
//            intent.putExtra("isActive", ScriptFragment().isActive)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return subscriptionModelList.size
    }

}
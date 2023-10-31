package com.example.cryptoapp.modual.strategy.adapter

import android.content.Context
import android.content.Intent
import android.service.autofill.Dataset
import android.util.Log
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.getLiveTopGainersResponseItem
import com.example.cryptoapp.modual.strategy.CoinDataset
import com.example.cryptoapp.modual.strategy.CoinSelectionActivity
import com.example.cryptoapp.modual.strategy.WelcomeActivity
import kotlin.math.log

class CoinSelectionAdapter(val context: Context, var list: ArrayList<getLiveTopGainersResponseItem>) :
    RecyclerView.Adapter<CoinSelectionAdapter.ViewHolder>() {

    var checkBoxStateArray = SparseBooleanArray()
    var coinSelectList : ArrayList<CoinData> = ArrayList()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cb_coin: CheckBox = itemView.findViewById(R.id.cb_coin)

        init {
            cb_coin.setOnClickListener {
                if (!checkBoxStateArray.get(adapterPosition, false)) {

                    cb_coin.isChecked = true
                    checkBoxStateArray.put(adapterPosition,true)
                    coinSelectList.add(CoinData(list.get(adapterPosition).symbol))
                } else {
                    cb_coin.isChecked = false
                    checkBoxStateArray.put(adapterPosition,false)
                }
            }
        }
    }

    fun customMethod(coin_selection:String,slider_price:Int,tradingType:Int,strategyId:String,userId:String) :Boolean {
        if(coinSelectList.size != 0){
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.putExtra("data",coinSelectList)
            intent.putExtra("coin_selection",coin_selection)
            intent.putExtra("slider_price",slider_price)
            intent.putExtra("tradingType",tradingType)
            intent.putExtra("strategyId",strategyId)
            intent.putExtra("userId",userId)
            context.startActivity(intent)
            coinSelectList.clear()
            return true
        }else{
            return false
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.coin_selection_adpter, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.cb_coin.isChecked = checkBoxStateArray.get(position, false)

       // var pos = list.get(position).position
        holder.cb_coin.text = "${list.get(position).symbol}"
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
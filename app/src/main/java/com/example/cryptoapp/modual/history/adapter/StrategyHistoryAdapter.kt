package com.example.cryptoapp.modual.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.cryptoapp.R

class StrategyHistoryAdapter (context: Context, private val dataList: List<Int>) :
    ArrayAdapter<Int>(context, android.R.layout.simple_spinner_item, dataList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.strategy_history_adapter, parent, false)

        val item = dataList[position]
        val textView = view.findViewById<TextView>(R.id.txt_doc_name)
        textView.text = item.toString()

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.strategy_history_adapter, parent, false)

        val item = dataList[position]
        val textView = view.findViewById<TextView>(R.id.txt_doc_name)
        textView.text = item.toString()

        return view
    }
}
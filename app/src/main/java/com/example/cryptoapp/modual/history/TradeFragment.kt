package com.example.cryptoapp.modual.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.history.adapter.TraderHistoryAdapter


class TradeFragment : Fragment() {

    lateinit var rv_trade : RecyclerView
    val fruits = arrayListOf("apple", "banana", "orange")
    lateinit var traderHistoryAdapter: TraderHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_trade, container, false)

        rv_trade = view.findViewById(R.id.rv_trade)


        rv_trade.layoutManager = LinearLayoutManager(activity)
//        traderHistoryAdapter =
//            TraderHistoryAdapter(
//                requireContext(),
//                fruits
//            )
//        rv_trade.adapter = traderHistoryAdapter
        return view
    }
}
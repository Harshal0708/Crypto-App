package com.example.cryptoapp.modual.dashbord

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.crypto.adapter.MarketAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CryptoFragment : Fragment() {

    lateinit var recyclerview: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_crypto, container, false)
        recyclerview = view.findViewById(R.id.crypto_recyclerView)
        getTopCurrencyList()
        return view
    }

    private fun getTopCurrencyList() {


        lifecycleScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.buildServiceTwo(RestApi::class.java).getMarketDate()
            withContext(Dispatchers.Main) {
                recyclerview.layoutManager = LinearLayoutManager(requireContext())
                recyclerview.adapter = MarketAdapter(requireContext(), response.body()!!.data.cryptoCurrencyList)
            }
            Log.d("test", "getTopCurrencyList : ${response.body()!!.data.cryptoCurrencyList.size}")

        }
    }
}
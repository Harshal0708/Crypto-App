package com.example.cryptoapp.modual.history

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.OrderHistoryDetailResponse
import com.example.cryptoapp.Response.OrderHistoryDetailResponseItem
import com.example.cryptoapp.modual.history.adapter.StrategyHistoryAdapter
import com.example.cryptoapp.modual.history.adapter.StrategyHistoryDetailAdapter
import com.example.cryptoapp.modual.history.adapter.SubscriptionHistoryAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class OrderStrategyFragment : Fragment() {

    lateinit var order_strategy_spinner: Spinner
    lateinit var rv_order_strategy: RecyclerView
    private lateinit var fragmentContext: Context
    lateinit var adapter: StrategyHistoryAdapter
    lateinit var num: ArrayList<Int>
    lateinit var response: Response<OrderHistoryDetailResponse>
    lateinit var strategyHistoryDetailAdapter: StrategyHistoryDetailAdapter
    var orderHistoryDetailResponseItem: ArrayList<OrderHistoryDetailResponseItem> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_order_strategy, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        order_strategy_spinner = view.findViewById(R.id.order_strategy_spinner) as Spinner
        rv_order_strategy = view.findViewById(R.id.rv_order_strategy)

        num = ArrayList()
        num.add(1)
        num.add(2)
        adapter = StrategyHistoryAdapter(fragmentContext, num)
        order_strategy_spinner.adapter = adapter

        order_strategy_spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                getStrategy2OrderHistoryDetail(num.get(position))

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }

        }

    }


    private fun getStrategy2OrderHistoryDetail(num: Int) {
//        viewLoader.visibility = View.VISIBLE

        orderHistoryDetailResponseItem.clear()

        lifecycleScope.launch(Dispatchers.IO) {

            if (num == 1) {
                response = ServiceBuilder(fragmentContext).buildService(RestApi::class.java)
                    .getStrategy1OrderHistoryDetail("050eec64-a803-4def-b0ec-6d82060c40fd")

            } else if (num == 2) {
                response = ServiceBuilder(fragmentContext).buildService(RestApi::class.java)
                    .getStrategy2OrderHistoryDetail("050eec64-a803-4def-b0ec-6d82060c40fd")

            }


            withContext(Dispatchers.Main) {
//                viewLoader.visibility = View.GONE
                Constants.showLog("Strategy2OrderHistory", response.body().toString())

                if (response.body()!!.size != 0) {
                    orderHistoryDetailResponseItem = response.body()!!
                    rv_order_strategy.layoutManager = LinearLayoutManager(activity)
                    strategyHistoryDetailAdapter =
                        StrategyHistoryDetailAdapter(
                            fragmentContext,
                            orderHistoryDetailResponseItem
                        )
                    rv_order_strategy.adapter = strategyHistoryDetailAdapter
                    strategyHistoryDetailAdapter.notifyDataSetChanged()

                } else {
                    // txt_subscription_data_not_found.visibility=View.VISIBLE
                }


            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

}
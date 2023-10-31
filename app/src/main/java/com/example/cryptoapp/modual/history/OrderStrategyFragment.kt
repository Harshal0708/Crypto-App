package com.example.cryptoapp.modual.history

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.Response.OrderHistoryDetailResponse
import com.example.cryptoapp.Response.OrderHistoryDetailResponseItem
import com.example.cryptoapp.Response.OrderHistoryDetailResponseItemX
import com.example.cryptoapp.modual.history.adapter.StrategyHistoryDetailAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class OrderStrategyFragment : Fragment() {

    lateinit var rv_order_strategy: RecyclerView
    private lateinit var fragmentContext: Context
    lateinit var response: Response<OrderHistoryDetailResponse>
    lateinit var strategyHistoryDetailAdapter: StrategyHistoryDetailAdapter

    var orderHistoryDetailResponseItem: ArrayList<OrderHistoryDetailResponseItemX> = ArrayList()

    lateinit var data: DataXX

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_order_strategy, container, false)
        init(view)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun init(view: View) {

        var preferences: MyPreferences
        preferences = MyPreferences(fragmentContext)
        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)
        rv_order_strategy = view.findViewById(R.id.rv_order_strategy)

        getStrategy2OrderHistoryDetail()

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun getStrategy2OrderHistoryDetail() {
//        viewLoader.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {


            response = ServiceBuilder(fragmentContext,false).buildService(RestApi::class.java)
                .getStrategy1OrderHistoryDetail(data.userId)

            withContext(Dispatchers.Main) {
//                viewLoader.visibility = View.GONE
//                Constants.showLog("GetFilledOrderHistory", response.body().toString())

                if (response.body() != null && response.body()!!.size != 0) {

                    orderHistoryDetailResponseItem =
                        response.body()!!
                    rv_order_strategy.layoutManager = LinearLayoutManager(fragmentContext)

                    strategyHistoryDetailAdapter =
                        StrategyHistoryDetailAdapter(
                            fragmentContext,
                            orderHistoryDetailResponseItem
                        )
                    rv_order_strategy.adapter = strategyHistoryDetailAdapter

                } else {
                    // txt_subscription_data_not_found.visibility=View.VISIBLE
                    showToast(fragmentContext,requireActivity(),"No data found")
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

}

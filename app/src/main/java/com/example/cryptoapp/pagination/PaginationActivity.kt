package com.example.cryptoapp.pagination

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.Response.OrderHistoriesResponse
import com.example.cryptoapp.Response.OrderHistory
import com.example.cryptoapp.model.GetOrderHistoryListPayload
import com.example.cryptoapp.modual.history.adapter.OrderHistoryAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaginationActivity : AppCompatActivity() {
    lateinit var preferences: MyPreferences
    lateinit var data: DataXX
    lateinit var orderHistories: ArrayList<OrderHistory>
    lateinit var orderHistoryAdapter: OrderHistoryAdapter
    var noLoading = true
    lateinit var layoutManager: LinearLayoutManager


    lateinit var rec_order_history: RecyclerView
    lateinit var txt_order_data_not_found: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagination)

        preferences = MyPreferences(this)
        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)

        rec_order_history = findViewById(R.id.rec_sub_history)
        txt_order_data_not_found = findViewById(R.id.txt_order_data_not_found)
        layoutManager = LinearLayoutManager(this)
        rec_order_history.layoutManager = layoutManager
        rec_order_history.itemAnimator = DefaultItemAnimator()

        orderHistories = ArrayList()
        orderHistoryAdapter = OrderHistoryAdapter(this@PaginationActivity, orderHistories)

        rec_order_history.adapter = orderHistoryAdapter
        getOrderHistoryList(0)
        addScrollListener()
    }

    private fun addScrollListener() {
        rec_order_history.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(noLoading && layoutManager.findLastCompletelyVisibleItemPosition() == orderHistories.size -1){
                    //orderHistories.add()
                    rec_order_history.adapter!!.notifyItemInserted(orderHistories.size-1)
                    noLoading=false


                    showLog("orderHistories.size-1",(orderHistories.size-1).toString())
                    val response = ServiceBuilder(this@PaginationActivity).buildService(RestApi::class.java)
                    var payload = GetOrderHistoryListPayload(
                        orderHistories.size-1,
                        1000,
                        data.userId
                    )

                    response.addOrderHistoryList(payload)
                        .enqueue(
                            object : Callback<OrderHistoriesResponse> {
                                override fun onResponse(
                                    call: Call<OrderHistoriesResponse>,
                                    response: Response<OrderHistoriesResponse>
                                ) {
                                    if (response.body()!!.isSuccess == true) {
//                            viewLoader.visibility = View.GONE
//                            isLoading == false

                                        orderHistories.removeAt(orderHistories.size-1)
                                        orderHistoryAdapter.notifyItemRemoved(orderHistories.size)
                                        if (response.body()!!.data.orderHistories.size != 0) {

                                            orderHistories.addAll(response.body()!!.data.orderHistories)
                                            orderHistoryAdapter.notifyDataSetChanged()
                                            noLoading=true

                                        } else {
                                            txt_order_data_not_found.visibility = View.GONE
                                        }

                                    } else {

                                        // viewLoader.visibility = View.GONE
                                        Constants.showToast(
                                            this@PaginationActivity,
                                            "End of data reached.."
                                        )

                                    }
                                }

                                override fun onFailure(call: Call<OrderHistoriesResponse>, t: Throwable) {
                                    // viewLoader.visibility = View.GONE
                                    Constants.showToast(
                                        this@PaginationActivity,
                                        getString(R.string.data_not_found)
                                    )
                                }

                            }
                        )
                }

                }

        })
    }


    fun getOrderHistoryList(pageNumber: Int) {

            showLog("test",pageNumber.toString())
//        viewLoader.visibility = View.VISIBLE
//        isLoading == true
        val response = ServiceBuilder(this).buildService(RestApi::class.java)
        var payload = GetOrderHistoryListPayload(
            pageNumber,
            1000,
            data.userId
        )

        response.addOrderHistoryList(payload)
            .enqueue(
                object : Callback<OrderHistoriesResponse> {
                    override fun onResponse(
                        call: Call<OrderHistoriesResponse>,
                        response: Response<OrderHistoriesResponse>
                    ) {
                        if (response.body()!!.isSuccess == true) {
//                            viewLoader.visibility = View.GONE
//                            isLoading == false

                            if (response.body()!!.data.orderHistories.size != 0) {
                                orderHistories.addAll(response.body()!!.data.orderHistories)
                                orderHistoryAdapter.notifyDataSetChanged()

                            } else {
                                txt_order_data_not_found.visibility = View.GONE
                            }

                        } else {

                           // viewLoader.visibility = View.GONE
                            Constants.showToast(
                                this@PaginationActivity,
                                getString(R.string.data_not_found)
                            )

                        }
                    }

                    override fun onFailure(call: Call<OrderHistoriesResponse>, t: Throwable) {
                       // viewLoader.visibility = View.GONE
                        Constants.showToast(
                           this@PaginationActivity,
                            getString(R.string.data_not_found)
                        )
                    }
                }
            )
    }
}
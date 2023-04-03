package com.example.cryptoapp.modual.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.Constants
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.Response.OrderHistoriesResponse
import com.example.cryptoapp.model.GetOrderHistoryListPayload
import com.example.cryptoapp.modual.history.adapter.OrderHistoryAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderFragment : Fragment() {

    lateinit var rec_order_history: RecyclerView
    lateinit var txt_order_data_not_found: TextView
    lateinit var orderHistoryAdapter: OrderHistoryAdapter
    lateinit var preferences: MyPreferences
    lateinit var data: DataXX
    lateinit var viewLoader: View
    lateinit var animationView: LottieAnimationView
    var pageNumber = 0
    var pageSize = 2000
    private var isLoading = false
    private var isLastPage = false
    val numberList : MutableList<String> = ArrayList()
    lateinit var layoutManager: LinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_order, container, false)
        InIt(view)
        return view
    }

    private fun InIt(view: View) {
        preferences = MyPreferences(requireContext())
        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)

        viewLoader = view.findViewById(R.id.viewLoader)
        animationView = viewLoader.findViewById(R.id.lotti_img)
        rec_order_history = view.findViewById(R.id.rec_sub_history)
        txt_order_data_not_found = view.findViewById(R.id.txt_order_data_not_found)
        layoutManager = LinearLayoutManager(activity)
        rec_order_history.layoutManager = layoutManager
        rec_order_history.itemAnimator =  DefaultItemAnimator()
        //rec_order_history.addItemDecoration(requireContext(),LinearLayoutManager.VERTICAL)

        setupAnim()

//        rec_order_history.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//
////                if(dy > 0){
////                    val visibleItemCount = layoutManager.childCount
////                    val pastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
////                    val total = orderHistoryAdapter.itemCount
////
////                 //   if(!isLoading){
////                        if((visibleItemCount + pastVisibleItem) >= total){
////                            pageNumber++
////                            getOrderHistoryList(pageNumber, pageSize)
////                        }
////                   // }
////                }
//
//                super.onScrolled(recyclerView, dx, dy)
//            }
//        })



        // Attach the pagination scroll listener
//        rec_order_history.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                val visibleItemCount = layoutManager.childCount
//                val totalItemCount = layoutManager.itemCount
//                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//
//                if (!isLoading && !isLastPage) {
//                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
//                        && firstVisibleItemPosition >= 0
//                        && totalItemCount >= pageSize
//                    ) {
//                        loadMoreItems()
//                    }
//                }
//            }
//        })
    //    loadData(pageNumber)
        getOrderHistoryList(pageNumber, pageSize)
    }

    private fun loadData(page: Int) {
        isLoading = true

        // Perform API call to get data for specified page
        // ...

        // Update the adapter with new data

        getOrderHistoryList(page, pageSize)

        isLoading = false

        // Check if this was the last page
        if (pageNumber == pageSize) {
            isLastPage = true
        } else {
            pageNumber++
        }
    }

    private fun loadMoreItems() {
        getOrderHistoryList(pageNumber, pageSize)
    }
    private fun setupAnim() {
        animationView.setAnimation(R.raw.currency)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    fun getOrderHistoryList(pageNumber: Int, pageSize: Int) {
        showLog("pageNumber",pageNumber.toString())
        showLog("pageSize",pageSize.toString())
        viewLoader.visibility = View.VISIBLE
        isLoading == true
        val response = ServiceBuilder(requireContext()).buildService(RestApi::class.java)
        var payload = GetOrderHistoryListPayload(
            pageNumber,
            pageSize,
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
                            viewLoader.visibility = View.GONE
                            isLoading == false

                            if (response.body()!!.data.orderHistories.size != 0) {

                                orderHistoryAdapter =
                                    context?.let {
                                        OrderHistoryAdapter(
                                            it,
                                            response.body()!!.data.orderHistories.reversed()
                                        )
                                    }!!
                                rec_order_history.adapter = orderHistoryAdapter
                            } else {
                                txt_order_data_not_found.visibility = View.GONE
                            }

                        } else {

                            viewLoader.visibility = View.GONE
                            Constants.showToast(
                                requireContext(),
                                getString(R.string.data_not_found)
                            )

                        }
                    }

                    override fun onFailure(call: Call<OrderHistoriesResponse>, t: Throwable) {
                        viewLoader.visibility = View.GONE
                        Constants.showToast(
                            requireContext(),
                            getString(R.string.data_not_found)
                        )
                    }
                }
            )
    }
}
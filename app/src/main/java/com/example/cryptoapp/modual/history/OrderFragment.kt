package com.example.cryptoapp.modual.history

import android.content.Context
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
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.Response.OrderHistoriesResponse
import com.example.cryptoapp.Response.OrderHistory
import com.example.cryptoapp.model.GetOrderHistoryListPayload
import com.example.cryptoapp.modual.history.adapter.OrderHistoryAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.pagination.OnLoadMoreListener
import com.example.cryptoapp.pagination.RecyclerViewLoadMoreScroll
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
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
    var pageNumber = 1
    var pageSize = 10
    private var isLoading = false
    private var isLastPage = false
    val numberList: MutableList<String> = ArrayList()
    lateinit var layoutManager: LinearLayoutManager

    lateinit var orderHistories: ArrayList<OrderHistory>
    var noLoading = true
    lateinit var scrollListener: RecyclerViewLoadMoreScroll

    private lateinit var fragmentContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_order, container, false)
        InIt(view)
        return view
    }

    private fun InIt(view: View) {
        preferences = MyPreferences(fragmentContext)
        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)

        viewLoader = view.findViewById(R.id.viewLoader)
        animationView = viewLoader.findViewById(R.id.lotti_img)
        rec_order_history = view.findViewById(R.id.rec_sub_history)
        txt_order_data_not_found = view.findViewById(R.id.txt_order_data_not_found)
        layoutManager = LinearLayoutManager(activity)
        rec_order_history.layoutManager = layoutManager
        rec_order_history.itemAnimator = DefaultItemAnimator()
        //rec_order_history.addItemDecoration(fragmentContext,LinearLayoutManager.VERTICAL)

        setupAnim()

        orderHistories = ArrayList()
        orderHistoryAdapter = context?.let { OrderHistoryAdapter(it, orderHistories) }!!


        rec_order_history.adapter = orderHistoryAdapter
        getOrderHistoryList(pageNumber, pageSize)
        //addScrollListener()
        setRVScrollListener()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }


    private fun setupAnim() {
        animationView.setAnimation(R.raw.currency)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    private fun setRVScrollListener() {

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                addScrollListener(pageNumber, pageSize)
            }
        })

        rec_order_history.addOnScrollListener(scrollListener)
    }

    private fun addScrollListener(number: Int, size: Int) {

//        showLog("test", pageNumber.toString())
        // orderHistoryAdapter.addLoadingView()
        pageNumber = number + 1
        pageSize = size + 10
        showLog("pageNumber", (pageNumber).toString())
        showLog("pageSize", (pageSize).toString())
        val response =
            ServiceBuilder(fragmentContext,false).buildService(RestApi::class.java)
        var payload = GetOrderHistoryListPayload(
            pageNumber,
            pageSize,
            data.userId
        )
//"2b490b8b-aba1-4a36-937b-08fee8821c16"
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

                            orderHistoryAdapter.removeLoadingView()
//                            orderHistories.removeAt(orderHistories.size - 1)
//                            orderHistoryAdapter.notifyItemRemoved(orderHistories.size)
                            if (response.body()!!.data.orderHistories.size != 0) {
                                orderHistories.addAll(response.body()!!.data.orderHistories)
                                //        orderHistoryAdapter.notifyDataSetChanged()
                                // noLoading = true
                                scrollListener.setLoaded()
                                //Update the recyclerView in the main thread
                                rec_order_history.post {
                                    orderHistoryAdapter.notifyDataSetChanged()
                                }
                            } else {
                                txt_order_data_not_found.visibility = View.GONE
                            }


                        } else {
                            // viewLoader.visibility = View.GONE

                            showToast(
                                fragmentContext,
                                requireActivity(),
                                "End of data reached.."
                            )

                        }
                    }

                    override fun onFailure(
                        call: Call<OrderHistoriesResponse>,
                        t: Throwable
                    ) {
                        // viewLoader.visibility = View.GONE

                        showToast(
                            fragmentContext,
                            requireActivity(),
                            getString(R.string.data_not_found)
                        )

                    }
                }
            )
//        rec_order_history.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                if (noLoading && layoutManager.findLastCompletelyVisibleItemPosition() == orderHistories.size - 1) {
//                    //orderHistories.add()
//                    rec_order_history.adapter!!.notifyItemInserted(orderHistories.size - 1)
//                    noLoading = false
//
//                    showLog("orderHistories.size-1", (orderHistories.size - 1).toString())
//
//                }
//
//            }
//
//        })
    }


    fun getOrderHistoryList(pageNumber: Int, pageSize: Int) {

        showLog("pageNumber", (pageNumber).toString())
        showLog("pageSize", (pageSize).toString())
        viewLoader.visibility = View.VISIBLE
//        isLoading == true
        val response = ServiceBuilder(fragmentContext,false).buildService(RestApi::class.java)
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
                                fragmentContext,
                                requireActivity(),
                                getString(R.string.data_not_found)
                            )

                        }
                    }

                    override fun onFailure(call: Call<OrderHistoriesResponse>, t: Throwable) {
                        // viewLoader.visibility = View.GONE
                        Constants.showToast(
                            fragmentContext,
                            requireActivity(),
                            getString(R.string.data_not_found)
                        )
                    }
                }
            )
    }
}
package com.example.cryptoapp.modual.history

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.Response.UserSubscriptionsResponse
import com.example.cryptoapp.model.GetOrderHistoryListPayload
import com.example.cryptoapp.modual.history.adapter.SubscriptionHistoryAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SubscriptionFragment : Fragment() {

    lateinit var rec_sub_history: RecyclerView
    lateinit var txt_subscription_data_not_found: TextView
    lateinit var subscriptionHistoryAdapter: SubscriptionHistoryAdapter
    lateinit var preferences: MyPreferences
    lateinit var data: DataXX
    lateinit var viewLoader: View
    lateinit var animationView: LottieAnimationView
    private lateinit var fragmentContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_subscription, container, false)
        InIt(view)
        return view
    }

    private fun InIt(view: View) {
        preferences = MyPreferences(fragmentContext)
        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)

        viewLoader = view.findViewById(R.id.viewLoader)
        animationView = viewLoader.findViewById(R.id.lotti_img)
        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)
        rec_sub_history = view.findViewById(R.id.rec_sub_history)
        txt_subscription_data_not_found = view.findViewById(R.id.txt_subscription_data_not_found)
        setupAnim()
        getSubscriptionHistoryList(0,10)

    }

    private fun setupAnim() {
        animationView.setAnimation(R.raw.currency)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    fun getSubscriptionHistoryList(pageNumber: Int, pageSize: Int) {
        viewLoader.visibility = View.VISIBLE
        val response = ServiceBuilder(fragmentContext,false).buildService(RestApi::class.java)
        var payload = GetOrderHistoryListPayload(
            pageNumber,
            pageSize,
            data.userId
        )

        response.addSubscriptionHistory(payload)
            .enqueue(
                object : Callback<UserSubscriptionsResponse> {
                    override fun onResponse(
                        call: Call<UserSubscriptionsResponse>,
                        response: Response<UserSubscriptionsResponse>
                    ) {
                        if (response.body()?.isSuccess == true) {
                            viewLoader.visibility = View.GONE

                            if(response.body()!!.data.userSubscriptions.size != 0){
                                rec_sub_history.layoutManager = LinearLayoutManager(activity)
                                subscriptionHistoryAdapter =
                                    SubscriptionHistoryAdapter(
                                        fragmentContext,
                                        response.body()!!.data.userSubscriptions
                                    )
                                rec_sub_history.adapter = subscriptionHistoryAdapter
                            }else{
                                txt_subscription_data_not_found.visibility=View.VISIBLE
                            }

                        } else {
                            viewLoader.visibility = View.GONE
                            Constants.showToast(
                                fragmentContext,
                                getString(R.string.data_not_found)
                            )
                        }
                    }

                    override fun onFailure(call: Call<UserSubscriptionsResponse>, t: Throwable) {
                        viewLoader.visibility = View.GONE
                        Constants.showToast(
                            fragmentContext,
                            getString(R.string.data_not_found)
                        )
                    }
                }
            )
    }
}
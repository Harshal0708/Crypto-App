package com.example.cryptoapp.modual.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    lateinit var subscriptionHistoryAdapter: SubscriptionHistoryAdapter
    lateinit var preferences: MyPreferences
    lateinit var data: DataXX
    lateinit var viewLoader: View
    lateinit var animationView: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_subscription, container, false)
        InIt(view)
        return view
    }

    private fun InIt(view: View) {
        preferences = MyPreferences(requireContext())
        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)

        viewLoader = view.findViewById(R.id.loader_animation)
        animationView = viewLoader.findViewById(R.id.lotti_img)
        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)
        rec_sub_history = view.findViewById(R.id.rec_sub_history)
        setupAnim()
        getSubscriptionHistoryList(0,10)

    }

    private fun setupAnim() {
        animationView.setAnimation(R.raw.currency)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    fun getSubscriptionHistoryList(pageNumber: Int, pageSize: Int) {
        viewLoader.visibility = View.VISIBLE
        val response = ServiceBuilder(requireContext()).buildService(RestApi::class.java)
        var payload = GetOrderHistoryListPayload(
            pageNumber,
            pageSize,
            data.userId
        )

        response.addSubscriptionHistoryList(payload)
            .enqueue(
                object : Callback<UserSubscriptionsResponse> {
                    override fun onResponse(
                        call: Call<UserSubscriptionsResponse>,
                        response: Response<UserSubscriptionsResponse>
                    ) {
                        if (response.body()?.isSuccess == true) {
                            viewLoader.visibility = View.GONE
                            rec_sub_history.layoutManager = LinearLayoutManager(activity)
                            subscriptionHistoryAdapter =
                                SubscriptionHistoryAdapter(
                                    requireContext(),
                                    response.body()!!.data.userSubscriptions
                                )
                            rec_sub_history.adapter = subscriptionHistoryAdapter
                        } else {
                            viewLoader.visibility = View.GONE
                            Constants.showToast(
                                requireContext(),
                                getString(R.string.data_not_found)
                            )
                        }

                    }

                    override fun onFailure(call: Call<UserSubscriptionsResponse>, t: Throwable) {
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
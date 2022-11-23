package com.example.cryptoapp.modual.login.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.*
import com.example.cryptoapp.model.ResetPayload
import com.example.cryptoapp.model.UserSubscriptionModel
import com.example.cryptoapp.modual.home.adapter.HomeAdapter
import com.example.cryptoapp.modual.login.LoginActivity
import com.example.cryptoapp.modual.subscription.SubscriptionModel
import com.example.cryptoapp.modual.subscription.adapter.SubscriptionAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ScriptFragment : Fragment(), View.OnClickListener {

    lateinit var txt_sub_monthly: TextView
    lateinit var txt_sub_quartly: TextView
    lateinit var txt_sub_yearly: TextView
    lateinit var rv_subsribtion: RecyclerView

    var subscriptionModelList: ArrayList<UserSubscriptionItem> = ArrayList()
    lateinit var subscriptionAdapter: SubscriptionAdapter
    lateinit var one: String
    lateinit var two: String
    lateinit var three: String

    lateinit var preferences: MyPreferences
    lateinit var userDetail : LoginUserDataResponse
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_script, container, false)

        init(view)
        return view
    }

    private fun init(view: View) {
        preferences = MyPreferences(requireContext())
        userDetail = Gson().fromJson(preferences.getLogin(), LoginUserDataResponse::class.java)

        txt_sub_monthly = view.findViewById(R.id.txt_sub_monthly)
        txt_sub_quartly = view.findViewById(R.id.txt_sub_quartly)
        txt_sub_yearly = view.findViewById(R.id.txt_sub_yearly)
        rv_subsribtion = view.findViewById(R.id.rv_subsribtion)

        txt_sub_monthly.setOnClickListener(this)
        txt_sub_quartly.setOnClickListener(this)
        txt_sub_yearly.setOnClickListener(this)

        getPlans()
        //getUserSubscriptionDetail()

    }


    private fun getPlans() {

        // viewLoader.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var response = ServiceBuilder.buildService(RestApi::class.java).getPlans()
            withContext(Dispatchers.Main) {
                //  viewLoader.visibility = View.GONE

                Log.d("test", "getPlans : ${response.body()}")
                txt_sub_monthly.text = response.body()?.get(0)?.planName
                txt_sub_quartly.text = response.body()?.get(1)?.planName
                txt_sub_yearly.text = response.body()?.get(2)?.planName
                one = response.body()?.get(0)?.id.toString()
                two = response.body()?.get(1)?.id.toString()
                three = response.body()?.get(2)?.id.toString()
                subscriptionModelList.clear()
                getUserSubscription(one)
            }
        }
    }

    fun getUserSubscription(id: String?) {

        //  register_progressBar?.visibility = View.VISIBLE
        val response = ServiceBuilder.buildService(RestApi::class.java)
        var payload = UserSubscriptionModel(
            id.toString(),
            "5215e06d-adf9-43c9-ec26-08dac88c409c",
            userDetail.userId
            )

        response.addUserSubscription(payload)
            .enqueue(
                object : Callback<UserSubscriptionResponse> {
                    override fun onResponse(
                        call: Call<UserSubscriptionResponse>,
                        response: Response<UserSubscriptionResponse>
                    ) {
                        subscriptionModelList = response.body()!!
                        ScriptAdapter(subscriptionModelList, id.toString())
                    }

                    override fun onFailure(call: Call<UserSubscriptionResponse>, t: Throwable) {
                        // register_progressBar?.visibility = View.GONE

                        Toast.makeText(activity, t.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                }
            )

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.txt_sub_monthly -> {

                txt_sub_monthly.setBackgroundResource(R.drawable.background_tab_primary_color)
                txt_sub_quartly.setBackgroundResource(0)
                txt_sub_yearly.setBackgroundResource(0)

                txt_sub_monthly.setTextColor(resources.getColor(R.color.white))
                txt_sub_quartly.setTextColor(resources.getColor(R.color.primary_color))
                txt_sub_yearly.setTextColor(resources.getColor(R.color.primary_color))


                subscriptionModelList.clear()
                getUserSubscription(one)

            }
            R.id.txt_sub_quartly -> {

                txt_sub_monthly.setBackgroundResource(0)
                txt_sub_quartly.setBackgroundResource(R.drawable.background_tab_primary_color)
                txt_sub_yearly.setBackgroundResource(0)


                txt_sub_monthly.setTextColor(resources.getColor(R.color.primary_color))
                txt_sub_quartly.setTextColor(resources.getColor(R.color.white))
                txt_sub_yearly.setTextColor(resources.getColor(R.color.primary_color))

                subscriptionModelList.clear()
                getUserSubscription(two)

            }
            R.id.txt_sub_yearly -> {

                txt_sub_monthly.setBackgroundResource(0)
                txt_sub_quartly.setBackgroundResource(0)
                txt_sub_yearly.setBackgroundResource(R.drawable.background_tab_primary_color)

                txt_sub_monthly.setTextColor(resources.getColor(R.color.primary_color))
                txt_sub_quartly.setTextColor(resources.getColor(R.color.primary_color))
                txt_sub_yearly.setTextColor(resources.getColor(R.color.white))

                subscriptionModelList.clear()
                getUserSubscription(three)

            }

        }

    }

    private fun ScriptAdapter(
        subscriptionModelList: ArrayList<UserSubscriptionItem>,
        planId:String
    ) {
        rv_subsribtion.layoutManager = LinearLayoutManager(requireContext())
        subscriptionAdapter = SubscriptionAdapter(
            requireContext(),
            subscriptionModelList,
            planId
        )
        rv_subsribtion.adapter = subscriptionAdapter

    }


}
package com.example.cryptoapp.modual.subscription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.Response.UserSubscriptionDetail
import com.example.cryptoapp.model.UserSubscriptionModel
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubscriptionDetailActivity : AppCompatActivity() {
    lateinit var txt_sub_detail_name :TextView
    lateinit var txt_sub_detail_price :TextView
    lateinit var txt_sub_detail_strategie :TextView
    lateinit var txt_sub_detail_is_active :TextView


    lateinit var preferences: MyPreferences
    lateinit var userDetail : DataXX

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription_detail)

        InIt()
    }

    private fun InIt() {

        preferences = MyPreferences(this)
        userDetail = Gson().fromJson(preferences.getLogin(), DataXX::class.java)

        txt_sub_detail_name = findViewById(R.id.txt_sub_detail_name)
        txt_sub_detail_price = findViewById(R.id.txt_sub_detail_price)
        txt_sub_detail_strategie = findViewById(R.id.txt_sub_detail_strategie)
        txt_sub_detail_is_active = findViewById(R.id.txt_sub_detail_is_active)


        getUserSubscriptionDetail()
    }


    fun getUserSubscriptionDetail() {

        //  register_progressBar?.visibility = View.VISIBLE
        val response = ServiceBuilder.buildService(RestApi::class.java)
        var payload = UserSubscriptionModel(intent.getStringExtra("planId").toString(),intent.getStringExtra("subscriptionId").toString(),userDetail.userId)

        response.addSubscriptionDetails(payload)
            .enqueue(
                object : Callback<UserSubscriptionDetail> {
                    override fun onResponse(
                        call: Call<UserSubscriptionDetail>,
                        response: Response<UserSubscriptionDetail>
                    ) {

                        //    register_progressBar?.visibility = View.GONE

                        txt_sub_detail_name.text=response.body()?.subscriptionName
                        txt_sub_detail_price.text=response.body()?.subscriptionPrice.toString()
                        txt_sub_detail_strategie.text=response.body()?.noOfStrategies.toString()
                        if(response.body()?.isActive == true){

                            txt_sub_detail_is_active.text="Active"

                        }else{

                            txt_sub_detail_is_active.text="Not Active"

                        }
                    }

                    override fun onFailure(call: Call<UserSubscriptionDetail>, t: Throwable) {
                        // register_progressBar?.visibility = View.GONE

                        Toast.makeText(this@SubscriptionDetailActivity, t.toString(), Toast.LENGTH_LONG)
                            .show()
                    }


                }
            )

    }

}
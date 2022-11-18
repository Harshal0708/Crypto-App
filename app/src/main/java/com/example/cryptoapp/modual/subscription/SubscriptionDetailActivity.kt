package com.example.cryptoapp.modual.subscription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.UserSubscriptionDetail
import com.example.cryptoapp.model.UserSubscriptionModel
import com.example.cryptoapp.modual.login.fragment.ScriptFragment
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubscriptionDetailActivity : AppCompatActivity() {
    lateinit var txt_sub_detail_name :TextView
    lateinit var txt_sub_detail_price :TextView
    lateinit var txt_sub_detail_strategie :TextView
    lateinit var txt_sub_detail_is_active :TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription_detail)

        InIt()
    }

    private fun InIt() {
        txt_sub_detail_name = findViewById(R.id.txt_sub_detail_name)
        txt_sub_detail_price = findViewById(R.id.txt_sub_detail_price)
        txt_sub_detail_strategie = findViewById(R.id.txt_sub_detail_strategie)
        txt_sub_detail_is_active = findViewById(R.id.txt_sub_detail_is_active)


//        txt_sub_detail_name.text=intent.getStringExtra("subscriptionName").toString()
//        txt_sub_detail_price.text=intent.getStringExtra("subscriptionPrice").toString()
//        txt_sub_detail_strategie.text=intent.getStringExtra("noOfStrategies").toString()
//        if(intent.getBooleanExtra("isActive",false) == true){
//
//            txt_sub_detail_is_active.text="Active"
//
//        }else{
//
//            txt_sub_detail_is_active.text="Not Active"
//
//        }
        getUserSubscriptionDetail()
    }


    fun getUserSubscriptionDetail() {

        //  register_progressBar?.visibility = View.VISIBLE
        val response = ServiceBuilder.buildService(RestApi::class.java)
        var payload = UserSubscriptionModel("47ae5465-6fa2-4f87-fe77-08dac88c21f7","5215e06d-adf9-43c9-ec26-08dac88c409c","b7cb0341-4f87-4ed9-8bb0-64a9cb2621f5")

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
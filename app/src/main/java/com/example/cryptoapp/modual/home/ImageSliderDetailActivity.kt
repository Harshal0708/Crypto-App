package com.example.cryptoapp.modual.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.CmsAdsAddResponse
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.model.CmsAdsAddPayload
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageSliderDetailActivity : AppCompatActivity() {

    lateinit var imageListId: String
    lateinit var preferences: MyPreferences

    lateinit var data: DataXX

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_slider_detail)

        init()
    }

    fun init() {
        //imageListId = intent.getStringExtra("imageListId").toString()
        preferences = MyPreferences(this)
        imageListId = "938fb7e9-f50d-44d1-f3e0-08daef072a64"
        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)

        getSubscriptionHistoryList(imageListId)
    }

    fun getSubscriptionHistoryList(userId: String) {
        // viewLoader.visibility = View.VISIBLE
        val response =
            ServiceBuilder(this@ImageSliderDetailActivity,false).buildService(RestApi::class.java)

        val payload = CmsAdsAddPayload(userId, false, data.userId)

        response.addCmsAdsAdd(payload)
            .enqueue(
                object : Callback<CmsAdsAddResponse> {
                    override fun onResponse(
                        call: Call<CmsAdsAddResponse>,
                        response: Response<CmsAdsAddResponse>
                    ) {
                        if (response.body()?.isSuccess == true) {
                            //viewLoader.visibility = View.GONE
                            showToast(
                                this@ImageSliderDetailActivity,
                                this@ImageSliderDetailActivity,
                                response.body()?.code.toString()
                            )
                        } else {
                            //viewLoader.visibility = View.GONE
                            showToast(
                                this@ImageSliderDetailActivity,
                                this@ImageSliderDetailActivity,
                                getString(R.string.data_not_found)
                            )
                        }
                    }

                    override fun onFailure(call: Call<CmsAdsAddResponse>, t: Throwable) {
                        //viewLoader.visibility = View.GONE
                        showToast(
                            this@ImageSliderDetailActivity,
                            this@ImageSliderDetailActivity,
                            getString(R.string.data_not_found)
                        )
                    }
                }
            )
    }
}
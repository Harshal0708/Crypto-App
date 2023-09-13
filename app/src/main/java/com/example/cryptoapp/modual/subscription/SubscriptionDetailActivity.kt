package com.example.cryptoapp.modual.subscription

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.CmsAdsAddResponse
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.Response.UserSubscriptionDetail
import com.example.cryptoapp.model.CreateUserSubscriptionPayload
import com.example.cryptoapp.model.UserSubscriptionModel
import com.example.cryptoapp.modual.card.CardActivity
import com.example.cryptoapp.modual.payment.PaymentActivity
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubscriptionDetailActivity : AppCompatActivity() , View.OnClickListener {
    lateinit var txt_sub_detail_name: TextView
    lateinit var txt_sub_detail_price: TextView
    lateinit var txt_sub_detail_strategie: TextView
    lateinit var txt_sub_detail_is_active: TextView

    lateinit var preferences: MyPreferences
    lateinit var userDetail: DataXX

    lateinit var viewLoader: View
    lateinit var animationView: LottieAnimationView

    lateinit var view: View
    lateinit var register_progressBar: ProgressBar
    lateinit var resent: TextView
    lateinit var progressBar_cardView: RelativeLayout

    lateinit var ima_back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription_detail)

        InIt()
    }

    private fun InIt() {

        preferences = MyPreferences(this)
        userDetail = Gson().fromJson(preferences.getLogin(), DataXX::class.java)
        viewLoader = findViewById(R.id.viewLoader)
        animationView = viewLoader.findViewById(R.id.lotti_img)
        txt_sub_detail_name = findViewById(R.id.txt_sub_detail_name)
        txt_sub_detail_price = findViewById(R.id.txt_sub_detail_price)
        txt_sub_detail_strategie = findViewById(R.id.txt_sub_detail_strategie)
        txt_sub_detail_is_active = findViewById(R.id.txt_sub_detail_is_active)


        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)
        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)

        ima_back = findViewById(R.id.ima_back)


        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.subscription)
        progressBar_cardView.setOnClickListener(this)
        ima_back.setOnClickListener(this)
        setupAnim()
        getUserSubscriptionDetail()

    }

    private fun setupAnim() {
        animationView.setAnimation(R.raw.currency)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }
    fun getUserSubscriptionDetail() {
        viewLoader.visibility = View.VISIBLE
        val response = ServiceBuilder(this@SubscriptionDetailActivity,false).buildService(RestApi::class.java)

        var payload = UserSubscriptionModel(
            intent.getStringExtra("planId").toString(),
            intent.getStringExtra("subscriptionId").toString(),
            userDetail.userId
        )

        response.addSubscriptionDetails(payload)
            .enqueue(
                object : Callback<UserSubscriptionDetail> {
                    override fun onResponse(
                        call: Call<UserSubscriptionDetail>,
                        response: Response<UserSubscriptionDetail>
                    ) {
                        viewLoader.visibility = View.GONE
                        txt_sub_detail_name.text = response.body()?.data?.subscriptionName
                        txt_sub_detail_price.text = response.body()?.data?.subscriptionPrice.toString()
                        txt_sub_detail_strategie.text = response.body()?.data?.noOfStrategies.toString()
                        if (response.body()?.data?.isActive == true) {
                            txt_sub_detail_is_active.text = "Active"
                        } else {
                            txt_sub_detail_is_active.text = "Not Active"
                        }
                    }

                    override fun onFailure(call: Call<UserSubscriptionDetail>, t: Throwable) {
                        viewLoader.visibility = View.GONE
                        showToast(
                            this@SubscriptionDetailActivity,
                            this@SubscriptionDetailActivity,
                            getString(R.string.data_not_found)
                        )
                    }
                }
            )

    }
    fun addCreateUserSubscription() {
        viewLoader.visibility = View.VISIBLE
        val response = ServiceBuilder(this@SubscriptionDetailActivity,false).buildService(RestApi::class.java)
        var payload = CreateUserSubscriptionPayload(
            intent.getStringExtra("planId").toString(),
            intent.getStringExtra("subscriptionId").toString(),
            userDetail.userId
        )

        response.addCreateUserSubscription(payload)
            .enqueue(
                object : Callback<CmsAdsAddResponse> {
                    override fun onResponse(
                        call: Call<CmsAdsAddResponse>,  
                        response: Response<CmsAdsAddResponse>
                    ) {
                        viewLoader.visibility = View.GONE

                        if (response.body()?.isSuccess == true) {
                            userDetail.haveAnySubscription = true
                            MyPreferences(this@SubscriptionDetailActivity).setLogin(userDetail)
                            val intent  = Intent(this@SubscriptionDetailActivity ,MainActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this@SubscriptionDetailActivity,"Subscription Done",Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@SubscriptionDetailActivity,"Please subscribe",Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onFailure(call: Call<CmsAdsAddResponse>, t: Throwable) {
                        viewLoader.visibility = View.GONE
                        showToast(
                            this@SubscriptionDetailActivity,
                            this@SubscriptionDetailActivity,
                            getString(R.string.data_not_found)
                        )
                    }
                }
            )
    }
    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.progressBar_cardView -> {
                addCreateUserSubscription()
//                val intent  = Intent(this@SubscriptionDetailActivity , PaymentActivity::class.java)
//                startActivity(intent)
            }
            R.id.ima_back -> {
                onBackPressed()
            }
        }
    }
}
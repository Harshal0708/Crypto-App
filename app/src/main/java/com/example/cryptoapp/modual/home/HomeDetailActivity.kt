package com.example.cryptoapp.modual.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.R
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeDetailActivity : AppCompatActivity() {

    lateinit var txt_sd_strategyName: TextView
    lateinit var txt_sd_description: TextView
    lateinit var txt_sd_minCapital: TextView
    lateinit var txt_sd_monthlyFee: TextView
    lateinit var txt_sd_createdDate: TextView
    lateinit var txt_sd_modifiedDate: TextView
    lateinit var txt_sd_status: TextView

    lateinit var viewLoader: View
    lateinit var animationView: LottieAnimationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_detail)

        InIt()
    }

    private fun InIt() {
        txt_sd_strategyName = findViewById(R.id.txt_sd_strategyName)
        txt_sd_description = findViewById(R.id.txt_sd_description)
        txt_sd_minCapital = findViewById(R.id.txt_sd_minCapital)
        txt_sd_monthlyFee = findViewById(R.id.txt_sd_monthlyFee)
        txt_sd_createdDate = findViewById(R.id.txt_sd_createdDate)
        txt_sd_modifiedDate = findViewById(R.id.txt_sd_modifiedDate)
        txt_sd_status = findViewById(R.id.txt_sd_status)

        viewLoader = findViewById(R.id.viewLoader)
        animationView = viewLoader.findViewById(R.id.lotti_img)
        setupAnim()
        getStrategyId(intent.getStringExtra("strategyId").toString())
    }

    private fun setupAnim() {
        animationView.setAnimation(R.raw.currency)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    private fun getStrategyId(id: String) {
        animationView.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var response = ServiceBuilder(this@HomeDetailActivity).buildService(RestApi::class.java).getStrategyById(id)
            withContext(Dispatchers.Main) {
                animationView.visibility = View.GONE
                txt_sd_strategyName.text = "Strategy Name :- ${response.body()!!.data.strategyName}"
                txt_sd_description.text = "Description :- ${response.body()!!.data.description}"
                txt_sd_minCapital.text = "Min Capital :- ${response.body()!!.data.minCapital}"
                txt_sd_monthlyFee.text = "Monthly Fee :- ${response.body()!!.data.monthlyFee}"
                txt_sd_createdDate.text = "Create Date :- ${response.body()!!.data.createdDate}"
                txt_sd_modifiedDate.text = "Modify Date :- ${response.body()!!.data.modifiedDate}"
                if(response.body()!!.data.isActive != true){
                    txt_sd_status.text = "Status :-Not Active"
                    txt_sd_status.setTextColor(resources.getColor(R.color.red))
                }else{
                    txt_sd_status.text = "Status :- Active"
                    txt_sd_status.setTextColor(resources.getColor(R.color.light_green))
                }
            }
        }
    }
}
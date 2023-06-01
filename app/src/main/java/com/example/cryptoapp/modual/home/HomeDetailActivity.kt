package com.example.cryptoapp.modual.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.Constants.Companion.showLog
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
    lateinit var txt_sd_minCapital_price: TextView
    lateinit var txt_sd_monthlyFee_price: TextView

    lateinit var viewLoader: View
    lateinit var toolbar: View
    lateinit var toolbar_img_back: ImageView

    lateinit var animationView: LottieAnimationView

    lateinit var progressBar_cardView: RelativeLayout

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
        txt_sd_minCapital_price = findViewById(R.id.txt_sd_minCapital_price)
        txt_sd_monthlyFee_price = findViewById(R.id.txt_sd_monthlyFee_price)

        toolbar = findViewById(R.id.toolbar)
        toolbar_img_back = toolbar.findViewById(R.id.toolbar_img_back)

        viewLoader = findViewById(R.id.viewLoader)

        animationView = viewLoader.findViewById(R.id.lotti_img)
        animationView.visibility = View.GONE

        setupAnim()
        //  getStrategyId(intent.getStringExtra("strategyId").toString())

        toolbar_img_back.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                onBackPressed()
                finish()
            }
        })

        txt_sd_strategyName.text = "Strategy 1"
        txt_sd_description.text = "Strategy Description : \n\n${getString(R.string.dummy_text)}"
        txt_sd_minCapital_price.text = "10.00"
        txt_sd_monthlyFee_price.text = "10.00"

    }

    private fun setupAnim() {
        animationView.setAnimation(R.raw.currency)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    private fun getStrategyId(id: String) {
        animationView.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var response = ServiceBuilder(this@HomeDetailActivity).buildService(RestApi::class.java)
                .getStrategyById(id)
            withContext(Dispatchers.Main) {
                animationView.visibility = View.GONE
                showLog("test", response.toString())

//                txt_sd_strategyName.text = "Strategy Name :- ${response.body()!!.data.strategyName}"
//                txt_sd_description.text = "Description :- ${response.body()!!.data.description}"
//                txt_sd_minCapital.text = "Min Capital :- ${response.body()!!.data.minCapital}"
//                txt_sd_monthlyFee.text = "Monthly Fee :- ${response.body()!!.data.monthlyFee}"

            }
        }
    }
}
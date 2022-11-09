package com.example.cryptoapp.modual.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.home.adapter.HomeAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.network.onItemClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeDetailActivity : AppCompatActivity() {
    lateinit var progressBar: ProgressBar
    lateinit var txt_sd_strategyName: TextView
    lateinit var txt_sd_description: TextView
    lateinit var txt_sd_minCapital: TextView
    lateinit var txt_sd_monthlyFee: TextView
    lateinit var txt_sd_createdDate: TextView
    lateinit var txt_sd_modifiedDate: TextView
    lateinit var txt_sd_status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_detail)

        InIt()
    }

    private fun InIt() {
        progressBar = findViewById(R.id.progressBar)
        txt_sd_strategyName = findViewById(R.id.txt_sd_strategyName)
        txt_sd_description = findViewById(R.id.txt_sd_description)
        txt_sd_minCapital = findViewById(R.id.txt_sd_minCapital)
        txt_sd_monthlyFee = findViewById(R.id.txt_sd_monthlyFee)
        txt_sd_createdDate = findViewById(R.id.txt_sd_createdDate)
        txt_sd_modifiedDate = findViewById(R.id.txt_sd_modifiedDate)
        txt_sd_status = findViewById(R.id.txt_sd_status)
        getStrategyId(intent.getIntExtra("strategyId", 0))
    }


    private fun getStrategyId(id: Int) {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var response = ServiceBuilder.buildService(RestApi::class.java).getStrategyById(id)
            withContext(Dispatchers.Main) {
                Log.d("test", "getStrategyById : ${response.body()}")
                progressBar.visibility = View.GONE
                txt_sd_strategyName.text = "Strategy Name :- ${response.body()!!.strategyName}"
                txt_sd_description.text = "Description :- ${response.body()!!.description}"
                txt_sd_minCapital.text = "Min Capital :- ${response.body()!!.minCapital}"
                txt_sd_monthlyFee.text = "Monthly Fee :- ${response.body()!!.monthlyFee}"
                txt_sd_createdDate.text = "Create Date :- ${response.body()!!.createdDate}"
                txt_sd_modifiedDate.text = "Modify Date :- ${response.body()!!.modifiedDate}"
                if(response.body()!!.isActive != true){
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
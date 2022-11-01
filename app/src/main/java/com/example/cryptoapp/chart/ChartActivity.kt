package com.example.cryptoapp.chart

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.login.LoginActivity

class ChartActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var pieChart: TextView
    lateinit var barChart: TextView
    lateinit var lineChart: TextView
    lateinit var groupBarChart: TextView
    lateinit var candleChart: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)


        pieChart = findViewById(R.id.pieChart)
        barChart = findViewById(R.id.barChart)
        lineChart = findViewById(R.id.lineChart)
        groupBarChart = findViewById(R.id.groupBarChart)
        candleChart = findViewById(R.id.candleChart)

        pieChart.setOnClickListener(this)
        barChart.setOnClickListener(this)
        lineChart.setOnClickListener(this)
        groupBarChart.setOnClickListener(this)
        candleChart.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.pieChart -> {
                var intent = Intent(this, PieActivity::class.java)
                startActivity(intent)
            }
            R.id.barChart -> {
                var intent = Intent(this, BarChartActivity::class.java)
                startActivity(intent)
            }


            R.id.lineChart -> {
                var intent = Intent(this, LineChartActivity::class.java)
                startActivity(intent)
            }

            R.id.groupBarChart -> {
                var intent = Intent(this, GroupBarChartActivity::class.java)
                startActivity(intent)
            }

            R.id.candleChart -> {
                var intent = Intent(this, CandleStickChartActivity::class.java)
                startActivity(intent)
            }


        }
    }
}
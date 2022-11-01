package com.example.cryptoapp.chart

import android.graphics.Color.green
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptoapp.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class LineChartActivity : AppCompatActivity() {

    lateinit var lineChart: LineChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_chart)

        lineChart = findViewById(R.id.lineChart)
        setLineChartData()
    }

    fun setLineChartData() {

        val linevalues = ArrayList<Entry>()
        linevalues.add(Entry(20f, 0.0F))
        linevalues.add(Entry(30f, 3.0F))
        linevalues.add(Entry(40f, 2.0F))
        linevalues.add(Entry(50f, 1.0F))
        linevalues.add(Entry(60f, 8.0F))
        linevalues.add(Entry(70f, 10.0F))
        linevalues.add(Entry(80f, 1.0F))
        linevalues.add(Entry(90f, 2.0F))
        linevalues.add(Entry(100f, 5.0F))
        linevalues.add(Entry(110f, 1.0F))
        linevalues.add(Entry(120f, 20.0F))
        linevalues.add(Entry(130f, 40.0F))
        linevalues.add(Entry(140f, 50.0F))

        val linedataset = LineDataSet(linevalues, "First")
        //We add features to our chart
        linedataset.color = resources.getColor(R.color.primary_color)

        linedataset.circleRadius = 10f
        linedataset.setDrawFilled(true)
        linedataset.valueTextSize = 20F
        linedataset.fillColor = resources.getColor(R.color.secondary_color)
        linedataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        //We connect our data to the UI Screen
        val data = LineData(linedataset)
        lineChart.data=data
        lineChart.setBackgroundColor(resources.getColor(R.color.white))
        lineChart.animateXY(2000, 2000, Easing.EaseInCubic)

    }
}
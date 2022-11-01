package com.example.cryptoapp.chart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptoapp.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class GroupBarChartActivity : AppCompatActivity() {

    lateinit var barChart: BarChart

    // on below line we are creating
    // a variable for bar data set
    lateinit var barDataSet1: BarDataSet
    lateinit var barDataSet2: BarDataSet

    // on below line we are creating array list for bar data
    lateinit var barEntriesList: ArrayList<BarEntry>

    // creating a string array for displaying days.
    var days = arrayOf("Sunday", "Monday", "Tuesday", "Thursday", "Friday", "Saturday")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_bar_chart)

        // on below line we are initializing
        // our variable with their ids.
        barChart = findViewById(R.id.idBarChart)

        // on below line we are creating a new bar data set
        barDataSet1 = BarDataSet(getBarChartDataForSet1(), "C++")
        barDataSet1.setColor(resources.getColor(R.color.primary_color))
        barDataSet2 = BarDataSet(getBarChartDataForSet2(), "Java")
        barDataSet2.setColor(resources.getColor(R.color.secondary_color))

        // on below line we are adding bar data set to bar data
        var data = BarData(barDataSet1, barDataSet2)

        // on below line we are setting data to our chart
        barChart.data = data

        // on below line we are setting description enabled.
        barChart.description.isEnabled = false

        // on below line setting x axis
        var xAxis = barChart.xAxis

        // below line is to set value formatter to our x-axis and
        // we are adding our days to our x axis.
        xAxis.valueFormatter = IndexAxisValueFormatter(days)

        // below line is to set center axis
        // labels to our bar chart.
        xAxis.setCenterAxisLabels(true)

        // below line is to set position
        // to our x-axis to bottom.
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // below line is to set granularity
        // to our x axis labels.
        xAxis.setGranularity(1f)

        // below line is to enable
        // granularity to our x axis.
        xAxis.setGranularityEnabled(true)

        // below line is to make our
        // bar chart as draggable.
        barChart.setDragEnabled(true)

        // below line is to make visible
        // range for our bar chart.
        barChart.setVisibleXRangeMaximum(3f)

        // below line is to add bar
        // space to our chart.
        val barSpace = 0.1f

        // below line is use to add group
        // spacing to our bar chart.
        val groupSpace = 0.5f

        // we are setting width of
        // bar in below line.
        data.barWidth = 0.15f

        // below line is to set minimum
        // axis to our chart.
        barChart.xAxis.axisMinimum = 0f

        // below line is to
        // animate our chart.
        barChart.animate()

        // below line is to group bars
        // and add spacing to it.
        barChart.groupBars(0f, groupSpace, barSpace)

        // below line is to invalidate
        // our bar chart.
        barChart.invalidate()

    }

    private fun getBarChartDataForSet1(): ArrayList<BarEntry> {
        barEntriesList = ArrayList()
        // on below line we are adding
        // data to our bar entries list
        barEntriesList.add(BarEntry(1f, 5f))
        barEntriesList.add(BarEntry(2f, 4f))
        barEntriesList.add(BarEntry(3f, 3f))
        barEntriesList.add(BarEntry(4f, 2f))
        barEntriesList.add(BarEntry(5f, 1f))
        return barEntriesList
    }

    private fun getBarChartDataForSet2(): ArrayList<BarEntry> {
        barEntriesList = ArrayList()
        // on below line we are adding data
        // to our bar entries list
        barEntriesList.add(BarEntry(1f, 2f))
        barEntriesList.add(BarEntry(2f, 4f))
        barEntriesList.add(BarEntry(3f, 6f))
        barEntriesList.add(BarEntry(4f, 8f))
        barEntriesList.add(BarEntry(5f, 10f))
        return barEntriesList
    }

}
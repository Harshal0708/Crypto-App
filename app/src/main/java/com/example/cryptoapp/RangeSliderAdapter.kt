package com.example.cryptoapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.network.onRangItemClickListener
import com.google.android.material.slider.Slider
import java.text.NumberFormat


class RangeSliderAdapter(
    private val rangeItems: List<RangeItem>,
    val onItemClickListener: onRangItemClickListener
) :
    RecyclerView.Adapter<RangeSliderAdapter.ViewHolder?>() {

    private var sliderPrice: Int = -1
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val itemView: View =
            LayoutInflater.from(context).inflate(R.layout.item_range_slider, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rangeItem = rangeItems[position]


        holder.range_slider.setLabelFormatter { value: Float ->
            val format = NumberFormat.getInstance()
            format.maximumFractionDigits = 0
//            format.currency = Currency.getInstance("USD")
            format.format(value.toDouble())
        }


        holder.range_slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {

            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {

            }

        })

        holder.range_slider.addOnChangeListener(
            Slider.OnChangeListener
            { slider, value, fromUser ->
                sliderPrice = slider.value.toInt()
                Constants.showLog("range_slider", sliderPrice.toString())
                onItemClickListener.onItemClick(position, rangeItems.get(position))
            })

        holder.text_title.text = rangeItem.title

//        Toast.makeText(context,sliderPrice.toString(),Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int {
        return rangeItems.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text_title: TextView = itemView.findViewById(com.example.cryptoapp.R.id.text_title)
        var range_slider: Slider = itemView.findViewById(com.example.cryptoapp.R.id.range_slider)
    }
}
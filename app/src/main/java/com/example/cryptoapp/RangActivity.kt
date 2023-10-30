package com.example.cryptoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.network.onRangItemClickListener
import com.example.cryptoapp.pagination.Utility.showErrorToast


class RangActivity : AppCompatActivity(), onRangItemClickListener {

    lateinit var recyclerView: RecyclerView
    var rangeItems: MutableList<RangeItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rang)

        recyclerView = findViewById(R.id.rv_recyclerView)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        val rangeItems = createRangeItems()

        val adapter = RangeSliderAdapter(rangeItems!!, onItemClickListener = this@RangActivity)
        recyclerView.adapter = adapter
    }

    private fun createRangeItems(): List<RangeItem>? {

        rangeItems.add(RangeItem("Item 1", 0, 100, 20, 80))
        rangeItems.add(RangeItem("Item 2", 0, 200, 30, 70))
        rangeItems.add(RangeItem("Item 3", 0, 300, 30, 70))

        // Add more items as needed
        return rangeItems
    }

    override fun onItemClick(pos: Int, rangeItem: RangeItem) {
        showErrorToast("${rangeItem.title + " Pos :-" + pos}")
    }
}
package com.example.cryptoapp.network

import com.example.cryptoapp.RangeItem

interface onRangItemClickListener {
    fun onItemClick(pos: Int, rangeItem: RangeItem)
}
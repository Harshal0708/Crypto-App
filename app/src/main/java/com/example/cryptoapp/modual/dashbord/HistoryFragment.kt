package com.example.cryptoapp.modual.dashbord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.history.*
import com.google.android.material.tabs.TabLayout


class HistoryFragment : Fragment() {

    lateinit var history_tab_layout :TabLayout
    lateinit var history_view_pager :ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view= inflater.inflate(R.layout.fragment_history, container, false)
        init(view)
        return view
    }

    private fun init(view: View){

        history_tab_layout = view.findViewById(R.id.history_tab_layout)
        history_view_pager = view.findViewById(R.id.history_view_pager)
        val adapter = HistoryViewPagerAdapter(requireActivity().supportFragmentManager)

        //adapter.addFragment(TradeFragment(), "Trade")
        adapter.addFragment(PositionFragment(), "Position")
        adapter.addFragment(OrderStrategyFragment(), "History")
        adapter.addFragment(SubscriptionFragment(), "Subscription")
        history_view_pager.adapter = adapter
        history_tab_layout.setupWithViewPager(history_view_pager)

    }
}
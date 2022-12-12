package com.example.cryptoapp.modual.dashbord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.subscription.SubscriptionHistoryModel
import com.example.cryptoapp.modual.subscription.adapter.SubHistoryAdapter


class HistoryFragment : Fragment() {


    var subscriptionHistoryModel :ArrayList<SubscriptionHistoryModel> = ArrayList()
    lateinit var rv_sub_history : RecyclerView
    lateinit var subHistoryAdapter: SubHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_history, container, false)
        init(view)
        return view
    }
    private fun init(view: View){
        rv_sub_history = view.findViewById(R.id.rv_sub_history)
        subscriptionHistoryModel.add(SubscriptionHistoryModel(1,1,"1/11/21","5/11/21","Strategies 1",1000,"Purchased"))
        subscriptionHistoryModel.add(SubscriptionHistoryModel(2,2,"5/11/21","10/11/21","Strategies 2",2000,"Completed"))
        subscriptionHistoryModel.add(SubscriptionHistoryModel(3,3,"10/11/21","15/11/21","Strategies 3",3000,"Completed"))
        subscriptionHistoryModel.add(SubscriptionHistoryModel(4,4,"10/11/21","15/11/21","Strategies 4",4000,"Completed"))
        subscriptionHistoryModel.add(SubscriptionHistoryModel(5,5,"15/11/21","20/11/21","Strategies 5",5000,"Completed"))
        subscriptionHistoryModel.add(SubscriptionHistoryModel(6,6,"20/11/21","25/11/21","Strategies 6",6000,"Completed"))
        subscriptionHistoryModel.add(SubscriptionHistoryModel(7,7,"25/11/21","30/11/21","Strategies 7",7000,"Completed"))
        rv_sub_history.layoutManager=  LinearLayoutManager(activity)
        subHistoryAdapter=SubHistoryAdapter(requireContext(),subscriptionHistoryModel)
        rv_sub_history.adapter=subHistoryAdapter

    }
}
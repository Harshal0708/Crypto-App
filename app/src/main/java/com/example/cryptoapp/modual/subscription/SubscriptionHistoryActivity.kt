package com.example.cryptoapp.modual.subscription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.subscription.adapter.SubHistoryAdapter

class SubscriptionHistoryActivity : AppCompatActivity() {

    var subscriptionHistoryModel :ArrayList<SubscriptionHistoryModel> = ArrayList()
    lateinit var rv_sub_history : RecyclerView
    lateinit var subHistoryAdapter: SubHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription_history)

        init()
    }
    private fun init(){
        rv_sub_history = findViewById(R.id.rv_sub_history)
        subscriptionHistoryModel.add(SubscriptionHistoryModel(1,1,"1/11/21","5/11/21","Strategies 1",1000,"Purchased"))
        subscriptionHistoryModel.add(SubscriptionHistoryModel(2,2,"5/11/21","10/11/21","Strategies 2",2000,"Completed"))
        subscriptionHistoryModel.add(SubscriptionHistoryModel(3,3,"10/11/21","15/11/21","Strategies 3",3000,"Completed"))
        subscriptionHistoryModel.add(SubscriptionHistoryModel(4,4,"10/11/21","15/11/21","Strategies 4",4000,"Completed"))
        subscriptionHistoryModel.add(SubscriptionHistoryModel(5,5,"15/11/21","20/11/21","Strategies 5",5000,"Completed"))
        subscriptionHistoryModel.add(SubscriptionHistoryModel(6,6,"20/11/21","25/11/21","Strategies 6",6000,"Completed"))
        subscriptionHistoryModel.add(SubscriptionHistoryModel(7,7,"25/11/21","30/11/21","Strategies 6",7000,"Completed"))
        rv_sub_history.layoutManager=  LinearLayoutManager(this)
        subHistoryAdapter=SubHistoryAdapter(this,subscriptionHistoryModel)
        rv_sub_history.adapter=subHistoryAdapter

    }
}
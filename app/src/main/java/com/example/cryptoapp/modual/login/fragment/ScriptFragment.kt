package com.example.cryptoapp.modual.login.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.subscription.SubscriptionModel
import com.example.cryptoapp.modual.subscription.adapter.SubscriptionAdapter


class ScriptFragment : Fragment(), View.OnClickListener {

    lateinit var txt_sub_monthly: TextView
    lateinit var txt_sub_quartly: TextView
    lateinit var txt_sub_yearly: TextView
    lateinit var rv_subsribtion: RecyclerView

    var subscriptionModelList: ArrayList<SubscriptionModel> = ArrayList()
    lateinit var subscriptionAdapter : SubscriptionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_script, container, false)

        init(view)
        return view
    }

    private fun init(view: View) {
        txt_sub_monthly = view.findViewById(R.id.txt_sub_monthly)
        txt_sub_quartly = view.findViewById(R.id.txt_sub_quartly)
        txt_sub_yearly = view.findViewById(R.id.txt_sub_yearly)
        rv_subsribtion = view.findViewById(R.id.rv_subsribtion)

        txt_sub_monthly.setOnClickListener(this)
        txt_sub_quartly.setOnClickListener(this)
        txt_sub_yearly.setOnClickListener(this)

        subscriptionModelList.add(SubscriptionModel("Free",0))
        subscriptionModelList.add(SubscriptionModel("Starter",300))
        subscriptionModelList.add(SubscriptionModel("Retail",1200))
        subscriptionModelList.add(SubscriptionModel("Retail +",2500))
        subscriptionModelList.add(SubscriptionModel("Creator",5000))
        subscriptionModelList.add(SubscriptionModel("Creator +",15000))

        ScriptAdapter(subscriptionModelList)

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.txt_sub_monthly->{

                txt_sub_monthly.setBackgroundResource(R.drawable.background_tab_primary_color)
                txt_sub_quartly.setBackgroundResource(0)
                txt_sub_yearly.setBackgroundResource(0)

                txt_sub_monthly.setTextColor(resources.getColor(R.color.white))
                txt_sub_quartly.setTextColor(resources.getColor(R.color.primary_color))
                txt_sub_yearly.setTextColor(resources.getColor(R.color.primary_color))


                subscriptionModelList.clear()
                subscriptionModelList.add(SubscriptionModel("Free",0))
                subscriptionModelList.add(SubscriptionModel("Starter",300))
                subscriptionModelList.add(SubscriptionModel("Retail",1200))
                subscriptionModelList.add(SubscriptionModel("Retail +",2500))
                subscriptionModelList.add(SubscriptionModel("Creator",5000))
                subscriptionModelList.add(SubscriptionModel("Creator +",15000))

                ScriptAdapter(subscriptionModelList)
            }
            R.id.txt_sub_quartly->{

                txt_sub_monthly.setBackgroundResource(0)
                txt_sub_quartly.setBackgroundResource(R.drawable.background_tab_primary_color)
                txt_sub_yearly.setBackgroundResource(0)


                txt_sub_monthly.setTextColor(resources.getColor(R.color.primary_color))
                txt_sub_quartly.setTextColor(resources.getColor(R.color.white))
                txt_sub_yearly.setTextColor(resources.getColor(R.color.primary_color))

                subscriptionModelList.clear()
                subscriptionModelList.add(SubscriptionModel("Free",0))
                subscriptionModelList.add(SubscriptionModel("Starter",850))
                subscriptionModelList.add(SubscriptionModel("Retail",3400))
                subscriptionModelList.add(SubscriptionModel("Retail +",7000))
                subscriptionModelList.add(SubscriptionModel("Creator",14000))
                subscriptionModelList.add(SubscriptionModel("Creator +",42000))

                ScriptAdapter(subscriptionModelList)

            }
            R.id.txt_sub_yearly->{

                txt_sub_monthly.setBackgroundResource(0)
                txt_sub_quartly.setBackgroundResource(0)
                txt_sub_yearly.setBackgroundResource(R.drawable.background_tab_primary_color)

                txt_sub_monthly.setTextColor(resources.getColor(R.color.primary_color))
                txt_sub_quartly.setTextColor(resources.getColor(R.color.primary_color))
                txt_sub_yearly.setTextColor(resources.getColor(R.color.white))

                subscriptionModelList.clear()
                subscriptionModelList.add(SubscriptionModel("Free",0))
                subscriptionModelList.add(SubscriptionModel("Starter",3000))
                subscriptionModelList.add(SubscriptionModel("Retail",12000))
                subscriptionModelList.add(SubscriptionModel("Retail +",25000))
                subscriptionModelList.add(SubscriptionModel("Creator",50000))
                subscriptionModelList.add(SubscriptionModel("Creator +",150000))

                ScriptAdapter(subscriptionModelList)

            }

        }

    }

    private fun ScriptAdapter(subscriptionModelList: ArrayList<SubscriptionModel>) {
        rv_subsribtion.layoutManager= LinearLayoutManager(requireContext())
        subscriptionAdapter = SubscriptionAdapter(requireContext(),subscriptionModelList)
        rv_subsribtion.adapter=subscriptionAdapter

    }
}
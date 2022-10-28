package com.example.cryptoapp.modual.dashbord

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.cryptoapp.R
import com.example.cryptoapp.chart.ChartActivity
import com.example.cryptoapp.modual.login.LoginActivity
import com.example.cryptoapp.modual.login.UserActivity


class HomeFragment : Fragment(), View.OnClickListener {

    var logout: TextView? = null
    var chart: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)

        logout = view?.findViewById(R.id.logout)
        chart = view?.findViewById(R.id.chart)
        logout?.setOnClickListener(this)
        chart?.setOnClickListener(this)
        return view
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.logout -> {
                var intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
                Toast.makeText(activity, "Logout Successfully", Toast.LENGTH_SHORT).show()
            }
            R.id.chart -> {
                var intent = Intent(activity, ChartActivity::class.java)
                startActivity(intent)

            }


        }
    }

}
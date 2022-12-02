package com.example.cryptoapp.modual.dashbord

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.home.HomeDetailActivity
import com.example.cryptoapp.modual.home.adapter.HomeAdapter
import com.example.cryptoapp.modual.login.ResetPasswordActivity
import com.example.cryptoapp.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment(), View.OnClickListener {

    lateinit var strategies_rv: RecyclerView
    lateinit var homeAdapter: HomeAdapter
    lateinit var viewLoader: View
    lateinit var animationView: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)

        init(view)
        return view
    }

    private fun init(view: View) {
        strategies_rv = view.findViewById(R.id.strategies_rv)
        viewLoader = view.findViewById(R.id.loader_animation)
        animationView = viewLoader.findViewById(R.id.lotti_img)

        setupAnim()
        //getStrategy()

    }

    private fun setupAnim() {
        animationView.setAnimation(R.raw.currency)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    private fun getStrategy() {

        viewLoader.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var response = ServiceBuilder.buildService(RestApi::class.java).getStrategy()
            withContext(Dispatchers.Main) {
                viewLoader.visibility = View.GONE
                strategies_rv.layoutManager = LinearLayoutManager(requireContext())
                homeAdapter = HomeAdapter(requireContext(), response.body()!!)
                strategies_rv.adapter = homeAdapter

            }
        }
    }



    override fun onClick(p0: View?) {
        when (p0!!.id) {

        }
    }

}


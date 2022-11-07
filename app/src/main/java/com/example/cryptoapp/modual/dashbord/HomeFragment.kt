package com.example.cryptoapp.modual.dashbord

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.home.HomeDetailActivity
import com.example.cryptoapp.modual.home.adapter.HomeAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.network.onItemClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment(), View.OnClickListener {

    lateinit var strategies_rv: RecyclerView
    lateinit var homeAdapter: HomeAdapter
    lateinit var progressBar :ProgressBar
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
        progressBar = view.findViewById(R.id.progressBar)
        getStrategy()
    }

    private fun getStrategy() {

        progressBar.visibility=View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var response = ServiceBuilder.buildService(RestApi::class.java).getStrategy()
            withContext(Dispatchers.Main) {
                progressBar.visibility=View.GONE
                strategies_rv.layoutManager = LinearLayoutManager(requireContext())
                homeAdapter = HomeAdapter(requireContext(), response.body()!!)
                strategies_rv.adapter = homeAdapter
                homeAdapter.setOnItemClickListener(object : onItemClickListener {
                    override fun onItemClick(pos: Int) {
                        val intent = Intent(requireContext(),HomeDetailActivity::class.java)
                        intent.putExtra("strategyId", response.body()!!.get(pos).id)
                        startActivity(intent)

                        //showDialog()
                    }

                })
                Log.d("test", "getStrategy : ${response.body()}")
            }
        }
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_homestatus_dialog)
        val txt_Paused = dialog.findViewById(R.id.txt_Paused) as TextView
        val txt_exited = dialog.findViewById(R.id.txt_exited) as TextView
        txt_Paused.text="Paused"
        txt_exited.text="Exited"
        txt_Paused.setOnClickListener {
            dialog.dismiss()
        }
        txt_exited.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

}


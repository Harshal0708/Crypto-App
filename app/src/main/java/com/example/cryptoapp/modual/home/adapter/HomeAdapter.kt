package com.example.cryptoapp.modual.home.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.StrategyDataRes
import com.example.cryptoapp.Response.StrategyWSResponseItem
import com.example.cryptoapp.modual.home.HomeDetailActivity
import com.example.cryptoapp.modual.subscription.SubscriptionActivity

class HomeAdapter(
    var context: Context,
    var strategyResList: ArrayList<StrategyWSResponseItem>,
    var haveAnySubscription: Boolean
) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var txt_strategies_name: TextView = itemView.findViewById(R.id.txt_strategies_name)
        var txt_strategies_desc: TextView = itemView.findViewById(R.id.txt_strategies_desc)
        var txt_strategies_monthly_fee_price: TextView = itemView.findViewById(R.id.txt_strategies_monthly_fee_price)
        var txt_strategies_monthly_capital_price: TextView = itemView.findViewById(R.id.txt_strategies_monthly_capital_price)

        //        var txt_strategies_by: TextView = itemView.findViewById(R.id.txt_strategies_by)
        var txt_strategies_time: TextView = itemView.findViewById(R.id.txt_strategies_time)
       var txt_status_active: TextView = itemView.findViewById(R.id.txt_status_active)
//        var img_strategies_menu: ImageView = itemView.findViewById(R.id.img_strategies_menu)
        var txt_pl: TextView = itemView.findViewById(R.id.txt_pl)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.home_model_list, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_strategies_name.text = strategyResList.get(position).Strategy.StrategyName
        holder.txt_pl.text = "Strategy PL : ${strategyResList.get(position).PL}"
        holder.txt_strategies_monthly_capital_price.text =
            strategyResList.get(position).Strategy.MinCapital.toString()
        holder.txt_strategies_monthly_fee_price.text =
            strategyResList.get(position).Strategy.MonthlyFee.toString()
        holder.txt_strategies_desc.text = strategyResList.get(position).Strategy.Description

        holder.txt_strategies_time.text = Constants.getDate(strategyResList.get(position).Strategy.CreatedDate)
        if (strategyResList.get(position).Strategy.IsActive != true) {
            holder.txt_status_active.text = context.resources.getString(R.string.not_active)
            holder.txt_status_active.setTextColor(context.resources.getColor(R.color.red))
        } else {
            holder.txt_status_active.text = context.resources.getString(R.string.active)
            holder.txt_status_active.setTextColor(context.resources.getColor(R.color.light_green))
        }
//
//        holder.img_strategies_menu.setOnClickListener {
//            showDialog()
//        }
//
        holder.txt_strategies_name.setOnClickListener {
            val intent = Intent(context, HomeDetailActivity::class.java)
            intent.putExtra("strategyId", strategyResList.get(position).Strategy.Id)
            context.startActivity(intent)
        }
//
//        holder.itemView.setOnClickListener{
//            if(haveAnySubscription==true){
//
//            } else {
//                showSubscribeDialog()
//            }
//        }


        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(context, HomeDetailActivity::class.java)
                //intent.putExtra("strategyId", strategyResList.get(position).Strategy.Id)
                context.startActivity(intent)
            }

        })
    }

    override fun getItemCount(): Int {
        return strategyResList.size
    }

    fun showDialog() {
        var dialog = Dialog(context)
        dialog.setContentView(R.layout.custom_homestatus_dialog)
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)
        val txt_Paused = dialog.findViewById(R.id.txt_Paused) as TextView
        val txt_exited = dialog.findViewById(R.id.txt_exited) as TextView
        var status = ""

        val viewDialog = dialog.findViewById(R.id.btn_progressBar_dialog) as View
        val register_progressBar = viewDialog.findViewById(R.id.register_progressBar) as ProgressBar
        val progressBar_cardView =
            viewDialog.findViewById(R.id.progressBar_cardView) as RelativeLayout
        register_progressBar.visibility = View.GONE
        val resent = viewDialog.findViewById(R.id.resent) as TextView
        resent.text = context.getString(R.string.next)
        progressBar_cardView.setOnClickListener {
            Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        txt_Paused.setOnClickListener {
            status = "Paused"
            txt_Paused.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_done_primary_color,
                0
            )
            txt_exited.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        }
        txt_exited.setOnClickListener {
            status = "Exited"
            txt_exited.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_done_primary_color,
                0
            )
            txt_Paused.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        }
        dialog.show()
    }

    fun showSubscribeDialog() {
        var dialog = Dialog(context)
        dialog.setContentView(R.layout.custom_subscribe_dialog)
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)
        val txt_Paused = dialog.findViewById(R.id.txt_please_subscribe) as TextView

        val viewDialog = dialog.findViewById(R.id.btn_progressBar_dialog) as View
        val register_progressBar = viewDialog.findViewById(R.id.register_progressBar) as ProgressBar
        val resent = viewDialog.findViewById(R.id.resent) as TextView
        register_progressBar.visibility = View.GONE
        resent.text = context.getString(R.string.subscription)

        resent.setOnClickListener {
            val intent = Intent(context, SubscriptionActivity::class.java)
            context.startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
    }
}
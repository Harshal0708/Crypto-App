package com.example.cryptoapp.modual.home.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.StrategyRes
import com.example.cryptoapp.modual.home.HomeDetailActivity

class HomeAdapter(var context: Context, var strategyResList: StrategyRes) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var txt_strategies_name: TextView = itemView.findViewById(R.id.txt_strategies_name)
        var txt_strategies_monthlyFees: TextView =
            itemView.findViewById(R.id.txt_strategies_monthlyFees)
        var txt_strategies_min_capital: TextView =
            itemView.findViewById(R.id.txt_strategies_min_capital)
        var txt_strategies_by: TextView = itemView.findViewById(R.id.txt_strategies_by)
        var txt_strategies_time: TextView = itemView.findViewById(R.id.txt_strategies_time)
        var txt_strategies_status: TextView = itemView.findViewById(R.id.txt_strategies_status)
        var img_strategies_menu: ImageView = itemView.findViewById(R.id.img_strategies_menu)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.home_model_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_strategies_name.text = strategyResList.data.get(position).strategyName
        holder.txt_strategies_min_capital.text =
            strategyResList.data.get(position).minCapital.toString()
        holder.txt_strategies_monthlyFees.text =
            strategyResList.data.get(position).monthlyFee.toString()
        holder.txt_strategies_by.text = strategyResList.data.get(position).description
        holder.txt_strategies_time.text =
            "Created By : ${strategyResList.data.get(position).createdDate}"

        if (strategyResList.data.get(position).isActive != true) {

            holder.txt_strategies_status.text = "Status :-Not Active"
            holder.txt_strategies_status.setTextColor(context.resources.getColor(R.color.red))
        } else {
            holder.txt_strategies_status.text = "Status :-Active"
            holder.txt_strategies_status.setTextColor(context.resources.getColor(R.color.light_green))
        }

        holder.img_strategies_menu.setOnClickListener {
            showDialog()

        }
        holder.txt_strategies_name.setOnClickListener {
            val intent = Intent(context, HomeDetailActivity::class.java)
            intent.putExtra("strategyId", strategyResList.data.get(position).id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return strategyResList.data.size
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
            txt_Paused.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_done_primary_color,0)
            txt_exited.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0)
        }
        txt_exited.setOnClickListener {
            status = "Exited"
            txt_exited.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_done_primary_color,0)
            txt_Paused.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0)
        }
        dialog.show()


    }


}
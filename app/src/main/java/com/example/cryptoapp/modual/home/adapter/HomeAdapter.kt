package com.example.cryptoapp.modual.home.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.StrategyDataRes
import com.example.cryptoapp.modual.home.HomeDetailActivity
import com.example.cryptoapp.modual.subscription.SubscriptionActivity
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomeAdapter(
    var context: Context,
    var strategyResList: List<StrategyDataRes>,
    var haveAnySubscription: Boolean

) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var txt_strategies_name: TextView = itemView.findViewById(R.id.txt_strategies_name)
        var txt_strategies_desc: TextView = itemView.findViewById(R.id.txt_strategies_desc)
        var txt_strategies_time: TextView = itemView.findViewById(R.id.txt_strategies_time)
        var txt_status_active: TextView = itemView.findViewById(R.id.txt_status_active)
        var txt_red_more: TextView = itemView.findViewById(R.id.txt_red_more)

        //        var img_strategies_menu: ImageView = itemView.findViewById(R.id.img_strategies_menu)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.home_model_list, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txt_strategies_name.text = strategyResList.get(position).strategyName

        holder.txt_strategies_desc.text = strategyResList.get(position).description

        holder.txt_strategies_time.text =
            Constants.getDate(strategyResList.get(position).createdDate)

        if (strategyResList.get(position).isActive != true) {
            holder.txt_status_active.text = context.resources.getString(R.string.not_active)
            holder.txt_status_active.setTextColor(ContextCompat.getColor(context,R.color.red)
            )
        } else {
            holder.txt_status_active.text = context.resources.getString(R.string.active)
            holder.txt_status_active.setTextColor(ContextCompat.getColor(context,R.color.light_green))
        }


        holder.txt_strategies_name.setOnClickListener {
            val intent = Intent(context, HomeDetailActivity::class.java)
            intent.putExtra("strategyId", strategyResList.get(position).id)
            context.startActivity(intent)
        }

        holder.txt_strategies_time.setOnClickListener {
            val intent = Intent(context, HomeDetailActivity::class.java)
            intent.putExtra("strategyId", strategyResList.get(position).id)
            context.startActivity(intent)
        }


        holder.txt_red_more.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                showBottomSheetDialog(strategyResList.get(position).description)
            }

        })
    }

    override fun getItemCount(): Int {
        return strategyResList.size
    }

    private fun showBottomSheetDialog(desc:String) {
        val dialog = BottomSheetDialog(context)
        val sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null)

        var bottom_txt_desc: TextView = sheetView.findViewById(R.id.bottom_txt_desc)
        bottom_txt_desc.text=desc

        dialog.setContentView(sheetView)
        dialog.show()
    }

    fun showDialog() {
        val dialog = Dialog(context)
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
        val dialog = Dialog(context)
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
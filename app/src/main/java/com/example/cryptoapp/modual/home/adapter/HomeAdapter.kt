package com.example.cryptoapp.modual.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.StrategyRes
import com.example.cryptoapp.network.onItemClickListener

class HomeAdapter(var context: Context, var strategyResList: StrategyRes) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }


    class ViewHolder(itemView: View, mListener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var txt_strategies_name: TextView = itemView.findViewById(R.id.txt_strategies_name)
        var txt_strategies_monthlyFees: TextView =
            itemView.findViewById(R.id.txt_strategies_monthlyFees)
        var txt_strategies_min_capital: TextView =
            itemView.findViewById(R.id.txt_strategies_min_capital)
        var txt_strategies_by: TextView = itemView.findViewById(R.id.txt_strategies_by)
        var txt_strategies_time: TextView = itemView.findViewById(R.id.txt_strategies_time)
        var txt_strategies_status: TextView = itemView.findViewById(R.id.txt_strategies_status)

        init {
            itemView.setOnClickListener {
                mListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.home_model_list, parent, false),
            mListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_strategies_name.text = strategyResList.get(position).strategyName
        holder.txt_strategies_min_capital.text =
            strategyResList.get(position).minCapital.toString()
        holder.txt_strategies_monthlyFees.text =
            strategyResList.get(position).monthlyFee.toString()
        holder.txt_strategies_by.text = strategyResList.get(position).description
        holder.txt_strategies_time.text =
            "Created By : ${strategyResList.get(position).createdDate}"

        if(strategyResList.get(position).isActive != true){

            holder.txt_strategies_status.text = "Status :-Not Active"
            holder.txt_strategies_status.setTextColor(context.resources.getColor(R.color.red))
        }else{
            holder.txt_strategies_status.text = "Status :-Active"
            holder.txt_strategies_status.setTextColor(context.resources.getColor(R.color.light_green))
        }

    }

    override fun getItemCount(): Int {
        return strategyResList.size
    }
}
package com.example.cryptoapp.modual.countries


import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.GetCountriesResponseItem
import com.example.cryptoapp.network.onItemClickListener


class CountriesAdapter(
    val context: Context,
    var getCountriesResponseItem: ArrayList<GetCountriesResponseItem>,
    var activity: Activity,
    val onItemClickListener: onItemClickListener
) :
    RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txt_country_code: TextView = itemView.findViewById(R.id.txt_country_code)
        var txt_country_name: TextView = itemView.findViewById(R.id.txt_country_name)
        var txt_country_prefix: TextView = itemView.findViewById(R.id.txt_country_prefix)
        var con_country: ConstraintLayout = itemView.findViewById(R.id.con_country)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.countries_adapter, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_country_code.text = getCountriesResponseItem.get(position).countryCode.toString()

        holder.txt_country_name.text = getCountriesResponseItem.get(position).countryName
        holder.txt_country_prefix.text = getCountriesResponseItem.get(position).countryPrefix

        val number = position // Replace with your actual number

        val backgroundColor: Int
        backgroundColor = if (number % 2 == 0) {
            // Number is even, so set background color to blue
            context.getResources().getColor(R.color.white)
        } else {
            // Number is odd, so set background color to green
            context.getResources().getColor(R.color.gray)
        }
       holder.con_country.setBackgroundColor(backgroundColor)

//        holder.con_country.setOnClickListener {
//            val intent = Intent(context, activity::class.java)
//            intent.putExtra(
//                "countryCode",
//                getCountriesResponseItem.get(position).countryCode.toString()
//            )
//            intent.putExtra("countryId", getCountriesResponseItem.get(position).id)
//            context.startActivity(intent)
//            (context as Activity).finish()
//
//        }

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return getCountriesResponseItem.size
    }
}
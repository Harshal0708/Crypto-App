package com.example.cryptoapp.modual.countries


import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.GetCountriesResponseItem
import com.example.cryptoapp.network.onItemClickListener
import com.squareup.picasso.Picasso


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
        var img_country_code: ImageView = itemView.findViewById(R.id.img_country_code)
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

        val number = position

        val backgroundColor: Int
        backgroundColor = if (number % 2 == 0) {
            context.getResources().getColor(R.color.country_code)
        } else {
            context.getResources().getColor(R.color.gray)
        }
       holder.con_country.setBackgroundColor(backgroundColor)

        Picasso.get()
            .load(getCountriesResponseItem.get(position).countryFlagURL)
            .placeholder(R.drawable.ic_app_icon)
            .error(R.drawable.ic_user)
            .into(holder.img_country_code)

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return getCountriesResponseItem.size
    }
}
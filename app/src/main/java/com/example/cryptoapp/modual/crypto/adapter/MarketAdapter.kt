package com.example.cryptoapp.modual.crypto.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.crypto.CryptoCurrency

class MarketAdapter(var context: Context, private val mList: List<CryptoCurrency>) : RecyclerView.Adapter<MarketAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from( context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        val img ="https://s2.coinmarketcap.com/static/img/coins/64x64/" + ItemsViewModel.id.toString() + ".png"

        Glide.with(context).load(img).into(holder.imageView)
        // sets the text to the textview from our itemHolder class
        holder.textView.text = ItemsViewModel.name

        if(ItemsViewModel.quotes!![0].percentChange24h > 0){
            holder.changeTxt.setTextColor(context.resources.getColor(R.color.light_green))
            holder.changeTxt.text = "+ ${ItemsViewModel.quotes[0].percentChange24h} %"
        }else{
            holder.changeTxt.setTextColor(context.resources.getColor(R.color.red))
            holder.changeTxt.text = "${ItemsViewModel.quotes[0].percentChange24h} %"
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val changeTxt: TextView = itemView.findViewById(R.id.changeTxt)
    }
}
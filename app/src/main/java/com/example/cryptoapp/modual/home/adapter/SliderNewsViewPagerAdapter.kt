package com.example.cryptoapp.modual.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.example.cryptoapp.R
import com.example.cryptoapp.WebviewActivity
import com.example.cryptoapp.modual.news.response.Item
import com.squareup.picasso.Picasso
import java.util.*

//class SliderNewsViewPagerAdapter (val context: Context, val imageList: ArrayList<NewsData>) : PagerAdapter() {

class SliderNewsViewPagerAdapter (val context: Context, val items: List<Item>) : PagerAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as CardView
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView: View = mLayoutInflater.inflate(R.layout.image_news_slider_item, container, false)

        val imageView: ImageView = itemView.findViewById<View>(R.id.idIVImage) as ImageView
        val txt_cms_slider_name: TextView = itemView.findViewById<View>(R.id.txt_cms_slider_name) as TextView

        Picasso.get()
            .load(items.get(position).image)
//            .resize(50, 50)
//            .centerCrop()
            .into(imageView)

        txt_cms_slider_name.text=items.get(position).title

        itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {

                val intent = Intent(context, WebviewActivity::class.java)
                intent.putExtra("newsUrl",items.get(position).url)
                context.startActivity(intent)
            }
        })

        Objects.requireNonNull(container).addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as CardView)
    }
}
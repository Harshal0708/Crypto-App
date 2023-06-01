package com.example.cryptoapp.modual.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.CmsAdsListResponseData
import java.util.*

class SliderNewsViewPagerAdapter (val context: Context, val imageList: ArrayList<NewsData>) : PagerAdapter() {

    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView: View = mLayoutInflater.inflate(R.layout.image_news_slider_item, container, false)

        val imageView: ImageView = itemView.findViewById<View>(R.id.idIVImage) as ImageView
        val txt_cms_slider_name: TextView = itemView.findViewById<View>(R.id.txt_cms_slider_name) as TextView

        imageView.setImageResource(R.drawable.ic_splash)

        txt_cms_slider_name.text=imageList.get(position).title

        itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {

//                val intent = Intent(context, ImageSliderDetailActivity::class.java)
//                intent.putExtra("imageListId",imageList.get(position).id)
//                context.startActivity(intent)

            }

        })

        Objects.requireNonNull(container).addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}
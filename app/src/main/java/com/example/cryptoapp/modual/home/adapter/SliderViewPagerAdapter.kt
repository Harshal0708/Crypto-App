package com.example.cryptoapp.modual.home.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.CmsAdsListResponseData
import com.example.cryptoapp.modual.home.ImageSliderDetailActivity
import java.util.*

import android.util.Base64
class SliderViewPagerAdapter (val context: Context, val imageList: List<CmsAdsListResponseData>) : PagerAdapter() {

    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView: View = mLayoutInflater.inflate(R.layout.image_slider_item, container, false)

        val imageView: ImageView = itemView.findViewById<View>(R.id.idIVImage) as ImageView
        val txt_cms_slider_name: TextView = itemView.findViewById<View>(R.id.txt_cms_slider_name) as TextView

       // imageView.setImageResource(R.drawable.ic_splash)

        imageView.setImageBitmap(convertStringToBitmap(imageList.get(position).image))


        txt_cms_slider_name.text=imageList.get(position).title

//        itemView.setOnClickListener(object :View.OnClickListener{
//            override fun onClick(p0: View?) {
//
////                val intent = Intent(context, ImageSliderDetailActivity::class.java)
////                intent.putExtra("imageListId",imageList.get(position).id)
////                context.startActivity(intent)
//
//            }
//        })

        Objects.requireNonNull(container).addView(itemView)

        return itemView
    }
    fun convertStringToBitmap(encodedString: String): Bitmap {
        val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }



    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        container.removeView(`object` as ConstraintLayout)
    }
}
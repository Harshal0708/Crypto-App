package com.example.cryptoapp.modual.login.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.cryptoapp.R


class ScriptFragment : Fragment(), View.OnClickListener {

    var card_1: LinearLayout? = null
    var card_2: LinearLayout? = null
    var card_3: LinearLayout? = null
    var item_selected1: ImageView? = null
    var item_selected2: ImageView? = null
    var item_selected3: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_script, container, false)

        init(view)
        return view
    }

    private fun init(view: View?) {
        card_1 = view?.findViewById(R.id.card_1)
        card_2 = view?.findViewById(R.id.card_2)
        card_3 = view?.findViewById(R.id.card_3)
        item_selected1 = view?.findViewById(R.id.item_selected1)
        item_selected2 = view?.findViewById(R.id.item_selected2)
        item_selected3 = view?.findViewById(R.id.item_selected3)

        card_1?.setOnClickListener(this)
        card_2?.setOnClickListener(this)
        card_3?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.card_1 -> {
                cardView1()
            }
            R.id.card_2 -> {
                cardView2()
            }
            R.id.card_3 -> {
                cardView3()
            }

        }
    }

    fun cardView1() {
        card_1?.setBackgroundColor(R.drawable.background_sub_card)
        card_2?.setBackgroundColor(0)
        card_3?.setBackgroundColor(0)
        item_selected1?.visibility = View.VISIBLE
        item_selected2?.visibility = View.GONE
        item_selected3?.visibility = View.GONE
    }

    fun cardView2() {
        card_1?.setBackgroundColor(0)
        card_2?.setBackgroundColor(R.drawable.background_sub_card)
        card_3?.setBackgroundColor(0)
        item_selected1?.visibility = View.GONE
        item_selected2?.visibility = View.VISIBLE
        item_selected3?.visibility = View.GONE
    }

    fun cardView3() {
        card_1?.setBackgroundColor(0)
        card_2?.setBackgroundColor(0)
        card_3?.setBackgroundColor(R.drawable.background_sub_card)
        item_selected1?.visibility = View.GONE
        item_selected2?.visibility = View.GONE
        item_selected3?.visibility = View.VISIBLE
    }


}
package com.example.cryptoapp.modual.login.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cryptoapp.modual.login.fragment.DocumentFragment
import com.example.cryptoapp.modual.login.fragment.ScriptFragment
import com.example.cryptoapp.modual.login.fragment.UserFragment
import java.lang.reflect.Array.newInstance


class ViewPagerAdapter(fragmentActivity: FragmentActivity, var colorList: ArrayList<Fragment>) :

    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return colorList.size
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return DocumentFragment()
            1 -> return UserFragment()
            2 -> return ScriptFragment()
            else -> return DocumentFragment()
        }
    }
}
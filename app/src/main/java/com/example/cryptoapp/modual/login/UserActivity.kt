package com.example.cryptoapp.modual.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ActivityUserBinding
import com.example.cryptoapp.modual.login.adapter.ViewPagerAdapter
import com.example.cryptoapp.modual.login.adapter.ZoomOutPageTransformer
import com.example.cryptoapp.modual.login.fragment.DocumentFragment
import com.example.cryptoapp.modual.login.fragment.ScriptFragment
import com.example.cryptoapp.modual.login.fragment.UserFragment


class UserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager2()
    }

    private fun setupViewPager2() {
        val colorList = ArrayList<Fragment>()
        colorList.add(UserFragment())
        colorList.add(DocumentFragment())
        colorList.add(ScriptFragment())

        binding.viewPager.adapter = ViewPagerAdapter(this, colorList)

        binding.viewPager.setPageTransformer(ZoomOutPageTransformer())
        binding.circleIndicator.setViewPager(binding.viewPager)
        binding.userSkip.setOnClickListener(this)
    }

    override fun onBackPressed() {
        val viewPager = binding.viewPager
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            binding.userSkip.id -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
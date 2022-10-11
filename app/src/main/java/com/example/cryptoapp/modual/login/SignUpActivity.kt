package com.example.cryptoapp.modual.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ActivityMainBinding
import com.example.cryptoapp.databinding.ActivitySignUpBinding
import com.example.cryptoapp.modual.login.adapter.ViewPagerAdapter
import com.example.cryptoapp.modual.login.adapter.ZoomOutPageTransformer
import com.example.cryptoapp.modual.login.fragment.DocumentFragment
import com.example.cryptoapp.modual.login.fragment.ScriptFragment
import com.example.cryptoapp.modual.login.fragment.UserFragment


class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_sign_up)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager2()
    }

    private fun setupViewPager2() {
        val colorList = arrayListOf(UserFragment(),DocumentFragment(),ScriptFragment())
        binding.viewPager.adapter = ViewPagerAdapter(this, colorList)

        binding.viewPager.setPageTransformer(ZoomOutPageTransformer())
    }

    override fun onBackPressed() {
        val viewPager = binding.viewPager
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

}
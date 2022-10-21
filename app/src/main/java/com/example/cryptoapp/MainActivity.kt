package com.example.cryptoapp

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cryptoapp.button.BtnLoadingProgressbar
import com.example.cryptoapp.modual.dashbord.HomeFragment
import com.example.cryptoapp.modual.dashbord.ProfileFragment
import com.example.cryptoapp.modual.dashbord.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(HomeFragment())
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView

        bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {

                R.id.home -> {
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.market -> {
                    loadFragment(ProfileFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.setting -> {
                    loadFragment(SettingFragment())
                    return@setOnNavigationItemReselectedListener
                }

            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
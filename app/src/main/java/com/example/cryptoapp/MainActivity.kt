package com.example.cryptoapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.cryptoapp.modual.dashbord.CryptoFragment
import com.example.cryptoapp.modual.dashbord.HomeFragment
import com.example.cryptoapp.modual.dashbord.ProfileFragment
import com.example.cryptoapp.modual.login.LoginActivity
import com.example.cryptoapp.modual.login.ProfileActivity
import com.example.cryptoapp.modual.login.ResetPasswordActivity
import com.example.cryptoapp.modual.subscription.SubscriptionActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    var drawerLayout: DrawerLayout? = null
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    lateinit var menuItem: MenuItem
    lateinit var compoundButton: CompoundButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        drawerLayout = findViewById(R.id.my_drawer_layout);
        navView = findViewById(R.id.navView);
        menuItem= navView.menu.findItem(R.id.nav_switch)
        compoundButton= menuItem.actionView as CompoundButton

        if(isDarkModeOn() == true){
            compoundButton.isChecked=true
        }else{
            compoundButton.isChecked=false

        }

        compoundButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }
        actionBarDrawerToggle = ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close)
        drawerLayout?.addDrawerListener(actionBarDrawerToggle)

        // Display the hamburger icon to launch the drawer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Call syncState() on the action bar so it'll automatically change to the back button when the drawer layout is open
        actionBarDrawerToggle.syncState()
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView

        loadFragment(HomeFragment())

        bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {

                R.id.home -> {
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.market -> {
                    loadFragment(CryptoFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.setting -> {
                    loadFragment(ProfileFragment())
                    return@setOnNavigationItemReselectedListener
                }
            }
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_dashboard -> {
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_reset_password -> {
                    val intent = Intent(this, ResetPasswordActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_subscription -> {
                    val intent = Intent(this, SubscriptionActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_logout -> {
                    var intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    false
                }
            }
        }

    }

    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item)
    }
}
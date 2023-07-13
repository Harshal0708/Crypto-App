package com.example.cryptoapp

import android.content.Intent
import android.content.res.Configuration
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.modual.dashbord.HistoryFragment
import com.example.cryptoapp.modual.dashbord.HomeFragment
import com.example.cryptoapp.modual.dashbord.SettingFragment
import com.example.cryptoapp.modual.login.LoginActivity
import com.example.cryptoapp.modual.login.ProfileActivity
import com.example.cryptoapp.modual.login.ResetPasswordActivity
import com.example.cryptoapp.modual.subscription.SubscriptionActivity
import com.example.cryptoapp.modual.watchlist.WatchlistFragment
import com.example.cryptoapp.preferences.MyPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {


    lateinit var bottomNav: BottomNavigationView
    var drawerLayout: DrawerLayout? = null
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    lateinit var menuItem: MenuItem
    lateinit var compoundButton: CompoundButton

    lateinit var nav_img: ImageView
    lateinit var nav_name: TextView
    lateinit var nav_email: TextView
    lateinit var txt_privacy: TextView

    lateinit var data: DataXX

    lateinit var toolbar: View
    lateinit var toolbar_img_back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var preferences: MyPreferences
        preferences = MyPreferences(this)

        drawerLayout = findViewById(R.id.my_drawer_layout)
        navView = findViewById(R.id.navView)
        val parentView: View = navView.getHeaderView(0)
        nav_img = parentView.findViewById(R.id.nav_img)
        nav_name = parentView.findViewById(R.id.nav_name)
        nav_email = parentView.findViewById(R.id.nav_email)
        menuItem = navView.menu.findItem(R.id.nav_switch)
        txt_privacy = findViewById(R.id.txt_privacy)

        toolbar = findViewById(R.id.toolbar)
        toolbar_img_back = toolbar.findViewById(R.id.toolbar_img_back)

        toolbar_img_back.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
             drawerLayout?.open()
            }
        })

        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)
        nav_name.text = data.name
        nav_email.text = data.email

        if (data.profilePicture != null && data.profilePicture != "") {
            nav_img.setImageBitmap(byteArrayToBitmap(data.profilePicture.toByteArray()))
        }

//        nav_name.text = "Apurva Patel"
//        nav_email.text = "patelappu1495@gmail.com"

        txt_privacy.setOnClickListener(object :OnClickListener{
            override fun onClick(p0: View?) {
                showToast(this@MainActivity,"Privacy")
            }
        })

        // nav_img.setImageBitmap(writeOnDrawable(R.drawable.background_edittext, "Apurva")?.bitmap)
//        val b = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
//        val c = Canvas(b)
//        val paint = Paint()
//        paint.setStyle(Paint.Style.FILL)
//        paint.setColor(Color.RED)
//        paint.setTextSize(20F)
//        c.drawText("Apurva", x.toFloat(), y.toFloat(), paint)
//        nav_img.setImageBitmap(b)

        compoundButton = menuItem.actionView as CompoundButton

        if (isDarkModeOn() == true) {
            compoundButton.isChecked = true
        } else {
            compoundButton.isChecked = false
        }

        compoundButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout?.addDrawerListener(actionBarDrawerToggle)

        // Display the hamburger icon to launch the drawer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Call syncState() on the action bar so it'll automatically change to the back button when the drawer layout is open
        actionBarDrawerToggle.syncState()
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView

        loadFragment(HomeFragment())

        bottomNav.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.home -> {
                    loadFragment(HomeFragment())
                    return@setOnItemSelectedListener true
                }

                R.id.watchlist -> {
                    loadFragment(WatchlistFragment())
                    return@setOnItemSelectedListener true
                }

                R.id.history -> {
                    loadFragment(HistoryFragment())
                    return@setOnItemSelectedListener true
                }

                R.id.setting -> {
                    loadFragment(SettingFragment())
                    return@setOnItemSelectedListener true
                }

            }

            return@setOnItemSelectedListener true
        }


        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.nav_dashboard -> {
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.nav_history -> {
                    loadFragment(HistoryFragment())
                    bottomNav.setSelectedItemId(R.id.history)
                    drawerLayout?.close()
                    true
                }

                R.id.nav_profile -> {
                    loadFragment(SettingFragment())
                    bottomNav.setSelectedItemId(R.id.setting)
                    drawerLayout?.close()
//                    var intent = Intent(this, ProfileActivity::class.java)
//                    startActivity(intent)
//                    finish()
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
                    preferences.setRemember(false)
                    preferences.setToken("")
                    preferences.setLogin(null)
                    var intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    showToast(this@MainActivity, getString(R.string.logout_successfully))
                    true
                }

                else -> {
                    false
                }

            }
        }

    }

    fun writeOnDrawable(drawableId: Int, text: String?): BitmapDrawable? {
        val bm =
            BitmapFactory.decodeResource(resources, drawableId).copy(Bitmap.Config.ARGB_8888, true)
        val paint = Paint()
        paint.setStyle(Paint.Style.FILL)
        paint.setColor(Color.BLACK)
        paint.setTextSize(20F)
        val canvas = Canvas(bm)
        canvas.drawText(text!!, 0F, bm.height / 2F, paint)
        return BitmapDrawable(bm)
    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        val decodeResponse: ByteArray = Base64.decode(data, Base64.DEFAULT or Base64.NO_WRAP)
        val bitmap = BitmapFactory.decodeByteArray(decodeResponse, 0, decodeResponse.size)
        return bitmap
    }

    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        // transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item)
    }
}
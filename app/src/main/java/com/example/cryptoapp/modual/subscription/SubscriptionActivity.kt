package com.example.cryptoapp.modual.subscription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.login.fragment.ScriptFragment

class SubscriptionActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription)

        onSubscription()
    }


    fun onSubscription() {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fram_subscription, ScriptFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
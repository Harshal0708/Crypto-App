package com.example.cryptoapp.modual.subscription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.login.fragment.ScriptFragment

class SubscriptionActivity : AppCompatActivity(), View.OnClickListener {

    private val fragmentManager = supportFragmentManager
    lateinit var ima_back: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription)

        ima_back = findViewById(R.id.ima_back)
        ima_back.setOnClickListener(this)

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

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.ima_back -> {
                onBackPressed()
            }
        }
    }
}

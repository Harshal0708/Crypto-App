package com.example.cryptoapp.modual.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.login.LoginActivity
import com.example.cryptoapp.modual.login.UserActivity
import com.example.cryptoapp.preferences.MyPreferences

class SplashActivity : AppCompatActivity() {

    lateinit var animationView: LottieAnimationView
    lateinit var preferences: MyPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        preferences = MyPreferences(this)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        animationView = findViewById(R.id.lotti_img)
        setupAnim()
        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.

        Handler().postDelayed({
            if(preferences.getRemember() == true){
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 3000) // 3000 is the delayed time in milliseconds.
    }

    private fun setupAnim() {
        animationView.setAnimation(R.raw.splash)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }
}
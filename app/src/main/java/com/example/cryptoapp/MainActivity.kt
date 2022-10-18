package com.example.cryptoapp

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoapp.button.BtnLoadingProgressbar

class MainActivity : AppCompatActivity() {
    private val handler = Handler()
    private var view : View?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         view = findViewById(R.id.activity_main_btn)

        view?.setOnClickListener {
            val progressbar = BtnLoadingProgressbar(it) // `it` is view of button
            progressbar.setLoading()
            handler.postDelayed({
                progressbar.setState(true){ // executed after animation end
                    handler.postDelayed({
                        startError(progressbar)
                    },1000)
                }
            },1500)
        }
    }

    private fun startError(progressbar: BtnLoadingProgressbar) {
        progressbar.reset()
        handler.postDelayed({
            progressbar.setLoading()
            handler.postDelayed({
                progressbar.setState(false){ // executed after animation end
                    handler.postDelayed({
                        progressbar.reset()
                    },1000)
                }
            },1500)
        },600)
    }
}
package com.example.cryptoapp.modual.login

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.cryptoapp.R

class ForgotPasswordActivity : AppCompatActivity() {

    var fp_btn_email : Button?=null
    var fp_btn_password : Button?=null
    var fp_et_email : EditText?=null
    var fp_et_password : EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        init()
        fp_btn_email!!.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fp_btn_email!!.setBackgroundColor(getColor(R.color.light_grey))
            }
            fp_btn_password!!.setBackgroundColor(0)
            fp_et_email!!.visibility=View.VISIBLE
            fp_et_password!!.visibility=View.GONE
        }

        fp_btn_password!!.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fp_btn_password!!.setBackgroundColor(getColor(R.color.light_grey))
            }
            fp_btn_email!!.setBackgroundColor(0)
            fp_et_email!!.visibility=View.GONE
            fp_et_password!!.visibility=View.VISIBLE
        }
    }

    fun init(){
        fp_btn_email = findViewById(R.id.fp_btn_email)
        fp_btn_password = findViewById(R.id.fp_btn_password)
        fp_et_email = findViewById(R.id.fp_et_email)
        fp_et_password = findViewById(R.id.fp_et_password)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fp_btn_email!!.setBackgroundColor(getColor(R.color.light_grey))
        }
        fp_btn_password!!.setBackgroundColor(0)
        fp_et_email!!.visibility=View.VISIBLE
        fp_et_password!!.visibility=View.GONE

    }
}
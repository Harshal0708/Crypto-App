package com.example.cryptoapp.modual.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.LoginResponse
import com.example.cryptoapp.model.LoginPayload
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder

class PasswordActivity : AppCompatActivity(), OnClickListener {
    var forgot_password: TextView? = null
    var pwd_emailOrPassword: TextView? = null
    var pwd_login: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        init()

    }

    fun init() {

        forgot_password = findViewById(R.id.forgot_password)
        pwd_emailOrPassword = findViewById(R.id.pwd_emailOrPassword)
        pwd_login = findViewById(R.id.pwd_login)

        pwd_login?.setOnClickListener(this)
        forgot_password?.setOnClickListener(this)
        pwd_emailOrPassword?.setText(intent.getStringExtra("emailOrPassword"))
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.pwd_login -> {
                var intent = Intent(this@PasswordActivity, UserActivity::class.java)
                startActivity(intent)
                finish()
                //addLogin()
            }
            R.id.forgot_password -> {
                val intent = Intent(this, ForgotPasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun addLogin() {

        val response = ServiceBuilder.buildService(RestApi::class.java)
        response.addLogin(loginPayload = LoginPayload("sachin.prajapati0533@gmail.com", "P@ssw0rd"))
            .enqueue(
                object : retrofit2.Callback<LoginResponse> {
                    override fun onResponse(
                        call: retrofit2.Call<LoginResponse>,
                        response: retrofit2.Response<LoginResponse>
                    ) {

                        var intent = Intent(this@PasswordActivity, UserActivity::class.java)
                        startActivity(intent)


                        Toast.makeText(
                            this@PasswordActivity,
                            response.body()!!.jwtToken,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@PasswordActivity, t.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                }
            )


    }


}
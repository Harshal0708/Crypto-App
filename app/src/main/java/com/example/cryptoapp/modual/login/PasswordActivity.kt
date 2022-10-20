package com.example.cryptoapp.modual.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.LoginResponse
import com.example.cryptoapp.model.LoginPayload
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.google.gson.Gson
import java.util.regex.Pattern

class PasswordActivity : AppCompatActivity(), OnClickListener {
    var forgot_password: TextView? = null
    var pwd_emailOrPassword: TextView? = null
    var reset_your_password: TextView? = null
    var pwd_login: TextView? = null
    var pwd_password: EditText? = null
    var register_progressBar: ProgressBar? = null

    val PASSWORD = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    )

    private lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        init()

    }

    fun init() {

        forgot_password = findViewById(R.id.forgot_password)
        pwd_emailOrPassword = findViewById(R.id.pwd_emailOrPassword)
        reset_your_password = findViewById(R.id.reset_your_password)
        pwd_login = findViewById(R.id.pwd_login)
        pwd_password = findViewById(R.id.pwd_password)
        register_progressBar = findViewById(R.id.register_progressBar)
        register_progressBar?.visibility = View.GONE
        pwd_login?.setOnClickListener(this)
        forgot_password?.setOnClickListener(this)
        reset_your_password?.setOnClickListener(this)
        pwd_emailOrPassword?.setText(intent.getStringExtra("emailOrPassword"))


        pwd_password?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var pwd = pwd_password?.text.toString().trim()


                if (!(PASSWORD.toRegex().matches(pwd))) {
                    pwd_password?.setError(getString(R.string.valid_password))
                }else{
                    Toast.makeText(this@PasswordActivity,"Password Verify Done!",Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.pwd_login -> {
//                val intent = Intent(this@PasswordActivity, UserActivity::class.java)
//                startActivity(intent)
//                finish()
                password = pwd_password?.text.toString()
                if (validation() == true) {
                    addLogin()
                }

            }
            R.id.forgot_password -> {
                val intent = Intent(this, ForgotPasswordActivity::class.java)
                startActivity(intent)
            }
            R.id.reset_your_password -> {
                val intent = Intent(this, ResetPasswordActivity::class.java)
                startActivity(intent)
            }

        }
    }


    fun validation(): Boolean {
        if (pwd_password?.length() == 0) {
            pwd_password?.setError(getString(R.string.password_error));
            return false;
        }

        return true

    }

    fun addLogin() {
        register_progressBar?.visibility = View.VISIBLE

        val response = ServiceBuilder.buildService(RestApi::class.java)

        var email: String = ""
        var mobile: String = ""
        if (intent.getBooleanExtra("isMobile", false) == false) {
            email = intent.getStringExtra("emailOrPassword").toString()
        } else {
            mobile = intent.getStringExtra("emailOrPassword").toString()
        }

        val payload =
            LoginPayload(
                "",
                "",
                email,
                password,
                mobile,
                "2022-10-18T07:55:16.737Z",
                "",
                "",
                true,
                ""
            )

        val gson = Gson()
        val json = gson.toJson(payload)
        Log.d("test", json)
        response.addLogin(payload)
            .enqueue(
                object : retrofit2.Callback<LoginResponse> {
                    override fun onResponse(
                        call: retrofit2.Call<LoginResponse>,
                        response: retrofit2.Response<LoginResponse>
                    ) {


                        Log.d("test", response.toString())
                        Log.d("test", response.body().toString())

                        if (response.body()?.code == "200") {
                            register_progressBar?.visibility = View.GONE
                            var intent = Intent(this@PasswordActivity, LoginOtpActivity::class.java)
                            intent.putExtra("phone",response.body()?.data?.mobile)
                            intent.putExtra("email",response.body()?.data?.email)
                            intent.putExtra("mobileOtp",response.body()?.data?.otp)
                            startActivity(intent)
                            finish()

                            Toast.makeText(
                                this@PasswordActivity,
                                response.body()?.message,
                                Toast.LENGTH_LONG
                            ).show()
                        } else {

                            register_progressBar?.visibility = View.GONE
                            Toast.makeText(
                                this@PasswordActivity,
                                "Not Login!",
                                Toast.LENGTH_LONG
                            ).show()
                        }


                    }

                    override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@PasswordActivity, t.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                }
            )


    }


}
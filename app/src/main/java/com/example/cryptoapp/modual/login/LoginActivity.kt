package com.example.cryptoapp.modual.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.LoginResponse
import com.example.cryptoapp.model.LoginPayload
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.google.gson.Gson
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity(), OnClickListener {

    lateinit var login_signUp: TextView
    lateinit var login_emailNumber: EditText
    private var isMobile: Boolean = false

    lateinit var view: View
    lateinit var register_progressBar: ProgressBar
    lateinit var resent: TextView
    lateinit var progressBar_cardView: RelativeLayout

    private lateinit var password: String

    lateinit var forgot_password: TextView
    lateinit var login_create: TextView

    val PASSWORD = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    )
    lateinit var pwd_password: EditText
//    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
//        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
//                "\\@" +
//                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
//                "(" +
//                "\\." +
//                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
//                ")+"
//    )


    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    )

    val PHONE_NUMBER_PATTERN = Pattern.compile(
        "[0-9]{10}"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    fun init() {
        login_signUp = findViewById(R.id.txt_sign_in_here)
        login_emailNumber = findViewById(R.id.login_emailNumber)
        pwd_password = findViewById(R.id.pwd_password)
        login_signUp.setOnClickListener(this)

        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)

        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)
        forgot_password = findViewById(R.id.forgot_password)
        login_create = findViewById(R.id.txt_otp_resend)

        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.login)
        progressBar_cardView.setOnClickListener(this)

        forgot_password.setOnClickListener(this)
        login_create.setOnClickListener(this)

        pwd_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var pwd = pwd_password.text.toString().trim()


                if (!(PASSWORD.toRegex().matches(pwd))) {
                    pwd_password.setError(getString(R.string.valid_password))
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Password Verify Done!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }


    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.progressBar_cardView -> {

                password = pwd_password.text.toString()
                if (btLogin() == true) {
//                    val intent = Intent(this, PasswordActivity::class.java)
//                    intent.putExtra("emailOrPassword", login_emailNumber.text.toString())
//                    intent.putExtra("isMobile", isMobile)
//                    startActivity(intent)
                    addLogin(login_emailNumber.text.toString())
                }
            }
            R.id.txt_sign_in_here -> {
                btSignup()
            }
            R.id.forgot_password -> {
                val intent = Intent(this, ForgotPasswordActivity::class.java)
                startActivity(intent)
            }
            R.id.txt_otp_resend -> {
                btSignup()
            }

        }
    }


    private fun addLogin(email: String) {
        register_progressBar.visibility = View.VISIBLE

        val response = ServiceBuilder.buildService(RestApi::class.java)

        var str_email = ""
        var str_mobile = ""
        if (isMobile == false) {
            str_email = email
        } else {
            str_mobile = email
        }

        val payload =
            LoginPayload(
                str_email,
                password,
                str_mobile,
                false
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
                            register_progressBar.visibility = View.GONE
                            var intent = Intent(this@LoginActivity, LoginOtpActivity::class.java)
                            intent.putExtra("phone", response.body()?.data?.mobile)
                            intent.putExtra("email", response.body()?.data?.email)
                            intent.putExtra("mobileOtp", response.body()?.data?.otp)
                            startActivity(intent)
                            finish()

                            Toast.makeText(
                                this@LoginActivity,
                                response.body()?.message,
                                Toast.LENGTH_LONG
                            ).show()
                        } else {

                            register_progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@LoginActivity,
                                "Not Login!",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }

                    override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, t.toString(), Toast.LENGTH_LONG)
                            .show()
                        Log.d("test", t.toString())
                    }

                }
            )
    }

    fun btLogin(): Boolean {
        // login_emailNumber.setText("9722183897")
        if (login_emailNumber.length() == 0) {
            login_emailNumber.setError(getString(R.string.valid_error));
            return false
        }


        val str_email = login_emailNumber.text.toString().trim()
        if (str_email.contains("@")) {
            isMobile = false
            if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(str_email))) {
                login_emailNumber.setError(getString(R.string.email_error));
                return false
            }
        } else {
            if (TextUtils.isDigitsOnly(str_email)) {
                isMobile = true
                if (!(PHONE_NUMBER_PATTERN.toRegex().matches(str_email))) {
                    login_emailNumber.setError(getString(R.string.phone_error));
                    return false
                }
            } else if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(str_email))) {
                login_emailNumber.setError(getString(R.string.email_error));
                return false
            }
        }

        if (pwd_password.length() == 0) {
            pwd_password.setError(getString(R.string.password_error));
            return false;
        }


        return true
    }


    fun btSignup() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }


}
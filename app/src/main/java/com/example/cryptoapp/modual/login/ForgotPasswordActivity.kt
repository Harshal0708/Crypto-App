package com.example.cryptoapp.modual.login

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.ForgotResponse
import com.example.cryptoapp.Response.RegisterResponse
import com.example.cryptoapp.model.ForgotPayload
import com.example.cryptoapp.model.RegisterPayload
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.google.gson.Gson
import retrofit2.Call
import java.util.regex.Pattern

class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {

    var forgot: TextView? = null
    var fp_et_email: EditText? = null
    var forgot_progressBar: ProgressBar? = null

    private lateinit var email: String

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        init()
    }

    fun init() {
        fp_et_email = findViewById(R.id.fp_et_email)
        forgot = findViewById(R.id.forgot)
        forgot_progressBar = findViewById(R.id.forgot_progressBar)
        forgot_progressBar?.visibility=View.GONE


        fp_et_email!!.visibility = View.VISIBLE

        forgot?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.forgot -> {
                email = fp_et_email?.text.toString()
                if (validation() == true) {
                    forgotPassword()
                }
            }
        }
    }

    private fun validation(): Any {
        if (fp_et_email?.length() == 0) {
            fp_et_email?.setError(getString(R.string.valid_error));
            return false;
        }

        email = fp_et_email?.text.toString().trim()
        if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(email))) {
            fp_et_email?.setError(getString(R.string.email_error));
            return false;
        }

        return true
    }


    fun forgotPassword() {

        forgot_progressBar?.visibility=View.VISIBLE
        val response = ServiceBuilder.buildService(RestApi::class.java)

        val payload = ForgotPayload(email)
        val gson = Gson()
        val json = gson.toJson(payload)

        Log.d("test", json)
//        response.addRegister(registerPayload = RegisterPayload(password,rePassword,email,firsName,lastName,"","","","","",phone,"Appu1111"))
        response.addForgotPassword(payload)
            .enqueue(
                object : retrofit2.Callback<ForgotResponse> {
                    override fun onResponse(
                        call: Call<ForgotResponse>,
                        response: retrofit2.Response<ForgotResponse>
                    ) {

                        Log.d("test", response.toString())
                        Log.d("test", response.body().toString())

                        if (response.body()?.code == "200") {
                            forgot_progressBar?.visibility=View.GONE
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                response.body()?.message,
                                Toast.LENGTH_LONG
                            ).show()
                            val intent =
                                Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {

                            forgot_progressBar?.visibility=View.GONE
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "Forgot Password not completed!",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }

                    override fun onFailure(call: Call<ForgotResponse>, t: Throwable) {
                        Log.d("test", t.toString())

                        forgot_progressBar?.visibility=View.GONE
                        Toast.makeText(this@ForgotPasswordActivity, t.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                }
            )


    }

}
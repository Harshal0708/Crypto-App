package com.example.cryptoapp.modual.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.ForgotResponse
import com.example.cryptoapp.Response.ResetResponse
import com.example.cryptoapp.model.ForgotPayload
import com.example.cryptoapp.model.ResetPayload
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.google.gson.Gson
import retrofit2.Call
import java.util.regex.Pattern

class ResetPasswordActivity : AppCompatActivity(), View.OnClickListener {

    var rp_et_email: EditText? = null
    var rp_et_password: EditText? = null
    var rp_et_rePassword: EditText? = null
    var resent: TextView? = null
    var register_progressBar: ProgressBar? = null

    private lateinit var email: String
    private lateinit var passowrd: String
    private lateinit var rePassowrd: String


    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    val PASSWORD = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        init()
    }


    fun init() {
        rp_et_email = findViewById(R.id.rp_et_email)
        rp_et_password = findViewById(R.id.rp_et_password)
        rp_et_rePassword = findViewById(R.id.rp_et_rePassword)
        resent = findViewById(R.id.resent)
        register_progressBar = findViewById(R.id.register_progressBar)
        register_progressBar?.visibility=View.GONE
        resent?.setOnClickListener(this)


        rp_et_password?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var pwd = rp_et_password?.text.toString().trim()


                if (!(PASSWORD.toRegex().matches(pwd))) {
                    rp_et_password?.setError(getString(R.string.valid_password))
                }else{
                    Toast.makeText(this@ResetPasswordActivity,"Password Verify Done!",Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        rp_et_rePassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var pwd = rp_et_rePassword?.text.toString().trim()


                if (!(PASSWORD.toRegex().matches(pwd))) {
                    rp_et_rePassword?.setError(getString(R.string.valid_password))
                }else{
                    Toast.makeText(this@ResetPasswordActivity,"Password Verify Done!",Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.resent -> {
                email = rp_et_email?.text.toString()
                passowrd = rp_et_password?.text.toString()
                rePassowrd = rp_et_rePassword?.text.toString()

                if (validation() == true) {
                    resentPassword()
                }
            }
        }
    }

    fun resentPassword() {

        register_progressBar?.visibility=View.VISIBLE
        val response = ServiceBuilder.buildService(RestApi::class.java)

        val payload = ResetPayload(email,rePassowrd)
        val gson = Gson()
        val json = gson.toJson(payload)

        Log.d("test", json)
//        response.addRegister(registerPayload = RegisterPayload(password,rePassword,email,firsName,lastName,"","","","","",phone,"Appu1111"))
        response.addResetpassword(payload)
            .enqueue(
                object : retrofit2.Callback<ResetResponse> {
                    override fun onResponse(
                        call: Call<ResetResponse>,
                        response: retrofit2.Response<ResetResponse>
                    ) {

                        Log.d("test", response.toString())
                        Log.d("test", response.body().toString())

                        if (response.body()?.code == "200") {
                            register_progressBar?.visibility=View.GONE
                            Toast.makeText(
                                this@ResetPasswordActivity,
                                response.body()?.message,
                                Toast.LENGTH_LONG
                            ).show()
                            val intent =
                                Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            register_progressBar?.visibility=View.GONE
                            Toast.makeText(
                                this@ResetPasswordActivity,
                                "Forgot Password not completed!",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }

                    override fun onFailure(call: Call<ResetResponse>, t: Throwable) {
                        register_progressBar?.visibility=View.GONE
                        Log.d("test", t.toString())
                        Toast.makeText(this@ResetPasswordActivity, t.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                }
            )


    }


    private fun validation(): Any {
        if (rp_et_email?.length() == 0) {
            rp_et_email?.setError(getString(R.string.valid_error));
            return false;
        }

        email = rp_et_email?.text.toString().trim()
        if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(email))) {
            rp_et_email?.setError(getString(R.string.email_error));
            return false;
        }

        if (rp_et_password?.length() == 0) {
            rp_et_password?.setError(getString(R.string.password_error));
            return false;
        }

        if (rp_et_rePassword?.length() == 0) {
            rp_et_rePassword?.setError(getString(R.string.repassword_error));
            return false;
        }

        if (!rp_et_password?.text.toString().equals(rp_et_rePassword?.text.toString())) {
            rp_et_rePassword?.setError(getString(R.string.password_not_error));
            return false;
        }

        return true
    }



}
package com.example.cryptoapp.modual.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.ResetResponse
import com.example.cryptoapp.model.ResetPayload
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import retrofit2.Call
import java.util.regex.Pattern

class ResetPasswordActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var rp_et_email: EditText
    lateinit var rp_et_password: EditText
    lateinit var rp_et_rePassword: EditText

    lateinit var view: View
    lateinit var register_progressBar: ProgressBar
    lateinit var resent: TextView
    lateinit var progressBar_cardView: RelativeLayout

    private lateinit var email: String
    private lateinit var passowrd: String
    private lateinit var rePassowrd: String
    lateinit var preferences: MyPreferences

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
        preferences = MyPreferences(this)
        rp_et_email = findViewById(R.id.rp_et_email)
        rp_et_password = findViewById(R.id.rp_et_password)
        rp_et_rePassword = findViewById(R.id.rp_et_rePassword)

        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)

        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)
        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.reset)
        progressBar_cardView.setOnClickListener(this)


        rp_et_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (rp_et_email.length() > 0) {
                    rp_et_email.setBackground(getResources().getDrawable(R.drawable.edt_bg_selected))
                } else {
                    rp_et_email.setBackground(getResources().getDrawable(R.drawable.edt_bg_normal))
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        rp_et_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var pwd = rp_et_password.text.toString().trim()

                if (rp_et_password.length() > 0) {
                    rp_et_password.setBackground(getResources().getDrawable(R.drawable.edt_bg_selected))
                } else {
                    rp_et_password.setBackground(getResources().getDrawable(R.drawable.edt_bg_normal))
                }

                if (!(PASSWORD.toRegex().matches(pwd))) {
                    rp_et_password.setError(getString(R.string.valid_password))
                } else {
                    showToast(this@ResetPasswordActivity, getString(R.string.password_verify_done))
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        rp_et_rePassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var pwd = rp_et_rePassword.text.toString().trim()


                if (rp_et_rePassword.length() > 0) {
                    rp_et_rePassword.setBackground(getResources().getDrawable(R.drawable.edt_bg_selected))
                } else {
                    rp_et_rePassword.setBackground(getResources().getDrawable(R.drawable.edt_bg_normal))
                }

                if (!(PASSWORD.toRegex().matches(pwd))) {
                    rp_et_rePassword.setError(getString(R.string.valid_password))
                } else {
                    showToast(this@ResetPasswordActivity, getString(R.string.password_verify_done))
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

                email = rp_et_email.text.toString()
                passowrd = rp_et_password.text.toString()
                rePassowrd = rp_et_rePassword.text.toString()

                if (validation() == true) {
                    resentPassword()
                }
            }
        }
    }

    fun resentPassword() {

        register_progressBar?.visibility = View.VISIBLE
        val response = ServiceBuilder(this@ResetPasswordActivity).buildService(RestApi::class.java)

        val payload = ResetPayload(email, rePassowrd)

        response.addResetpassword(payload)
            .enqueue(
                object : retrofit2.Callback<ResetResponse> {
                    override fun onResponse(
                        call: Call<ResetResponse>,
                        response: retrofit2.Response<ResetResponse>
                    ) {
                        if (response.body()?.isSuccess == true) {
                            register_progressBar?.visibility = View.GONE

                            response.body()?.message?.let {
                                showToast(this@ResetPasswordActivity,
                                    it
                                )
                            }
                            preferences.setRemember(false)
                            preferences.setToken("")
                            preferences.setLogin(null)
                            val intent =
                                Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {

                            register_progressBar?.visibility = View.GONE

                            response.body()?.message?.let {
                                showToast(this@ResetPasswordActivity,
                                    it
                                )
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResetResponse>, t: Throwable) {
                        register_progressBar?.visibility = View.GONE
                        showToast(this@ResetPasswordActivity, getString(R.string.reset_password_not_completed))
                    }

                }
            )
    }


    private fun validation(): Any {
        if (rp_et_email.length() == 0) {
            rp_et_email.setError(getString(R.string.valid_error));
            return false;
        }

        email = rp_et_email.text.toString().trim()
        if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(email))) {
            rp_et_email.setError(getString(R.string.email_error));
            return false;
        }

        if (rp_et_password.length() == 0) {
            rp_et_password.setError(getString(R.string.password_error));
            return false;
        }

        if (rp_et_rePassword.length() == 0) {
            rp_et_rePassword.setError(getString(R.string.repassword_error));
            return false;
        }

        if (!rp_et_password.text.toString().equals(rp_et_rePassword.text.toString())) {
            rp_et_rePassword.setError(getString(R.string.password_not_error));
            return false;
        }

        return true
    }
}

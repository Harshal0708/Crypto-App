package com.example.cryptoapp.modual.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.cryptoapp.R
import com.example.cryptoapp.button.BtnLoadingProgressbar
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity(), OnClickListener {
    private val handler = Handler()
    var login_btn_next: Button? = null
    var login_signUp: TextView? = null
    var btn_loading_layout_tv: TextView? = null
    var login_emailNumber: EditText? = null
    private var view: View? = null
    private var isMobile: Boolean = false
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
        login_btn_next = findViewById(R.id.login_btn_next)
        login_signUp = findViewById(R.id.login_signUp)
        login_emailNumber = findViewById(R.id.login_emailNumber)
        view = findViewById(R.id.activity_main_btn)
        btn_loading_layout_tv = view?.findViewById(R.id.btn_loading_layout_tv)
        btn_loading_layout_tv?.setText(getString(R.string.next))

        login_btn_next?.setOnClickListener(this)
        login_signUp?.setOnClickListener(this)
        btn_loading_layout_tv?.setOnClickListener(this)

        view?.setOnClickListener {
            val progressbar = BtnLoadingProgressbar(it)

            if (btLogin() == true) {
                startSuccess(progressbar)
            } else {
                startError(progressbar)
            }

        }
    }

    private fun startSuccess(progressbar: BtnLoadingProgressbar) {
        // `it` is view of button
        progressbar.setLoading()
        handler.postDelayed({
            progressbar.setState(true) { // executed after animation end
                handler.postDelayed({
                    progressbar.reset()
                    val intent = Intent(this, PasswordActivity::class.java)
                    intent.putExtra("emailOrPassword", login_emailNumber?.text.toString())
                    intent.putExtra("isMobile", isMobile)
                    startActivity(intent)
                }, 1500)
            }
        }, 2000)
    }

    private fun startError(progressbar: BtnLoadingProgressbar) {
        progressbar.reset()
        handler.postDelayed({
            progressbar.setLoading()
            handler.postDelayed({
                progressbar.setState(false) { // executed after animation end
                    handler.postDelayed({
                        progressbar.reset()
                    }, 1500)
                }
            }, 2000)
        }, 600)
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.login_btn_next -> {
                btLogin()
            }
            R.id.login_signUp -> {
                btSignup()
            }
        }
    }

    fun btLogin(): Boolean {
       // login_emailNumber?.setText("9722183897")
        if (login_emailNumber?.length() == 0) {
            login_emailNumber?.setError(getString(R.string.valid_error));
            return false;
        }


        if (login_emailNumber?.text.toString().contains("@")) {
            val str_email = login_emailNumber?.text.toString().trim()
            isMobile = false
            if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(str_email))) {
                login_emailNumber?.setError(getString(R.string.email_error));
                return false;
            }
        } else {
            if (TextUtils.isDigitsOnly(login_emailNumber?.text.toString())) {
                val str_number = login_emailNumber?.text.toString().trim()
                isMobile = true
                if (!(PHONE_NUMBER_PATTERN.toRegex().matches(str_number))) {
                    login_emailNumber?.setError(getString(R.string.phone_error));
                    return false;
                }
            }
        }

        return true
    }


    fun btSignup() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }


}
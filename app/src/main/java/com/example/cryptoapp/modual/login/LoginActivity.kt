package com.example.cryptoapp.modual.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.cryptoapp.R
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity(), OnClickListener {

   lateinit var login_signUp: TextView
    lateinit var login_emailNumber: EditText
    private var isMobile: Boolean = false

    lateinit var view: View
    lateinit var register_progressBar: ProgressBar
    lateinit var resent: TextView
    lateinit var progressBar_cardView: RelativeLayout

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
        login_signUp = findViewById(R.id.login_signUp)
        login_emailNumber = findViewById(R.id.login_emailNumber)

        login_signUp.setOnClickListener(this)

        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)

        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)
        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.next)
        progressBar_cardView.setOnClickListener(this)


    }


    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.progressBar_cardView -> {
                if (btLogin() == true) {
                    val intent = Intent(this, PasswordActivity::class.java)
                    intent.putExtra("emailOrPassword", login_emailNumber.text.toString())
                    intent.putExtra("isMobile", isMobile)
                    startActivity(intent)
                }
            }
            R.id.login_signUp -> {
                btSignup()
            }
        }
    }

    fun btLogin(): Boolean {
       // login_emailNumber.setText("9722183897")
        if (login_emailNumber.length() == 0) {
            login_emailNumber.setError(getString(R.string.valid_error));
            return false
        }


        if (login_emailNumber.text.toString().contains("@")) {
            val str_email = login_emailNumber.text.toString().trim()
            isMobile = false
            if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(str_email))) {
                login_emailNumber.setError(getString(R.string.email_error));
                return false
            }
        } else {
            if (TextUtils.isDigitsOnly(login_emailNumber.text.toString())) {
                val str_number = login_emailNumber.text.toString().trim()
                isMobile = true
                if (!(PHONE_NUMBER_PATTERN.toRegex().matches(str_number))) {
                    login_emailNumber.setError(getString(R.string.phone_error));
                    return false
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
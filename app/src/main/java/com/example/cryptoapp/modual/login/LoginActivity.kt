package com.example.cryptoapp.modual.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.LoginResponse
import com.example.cryptoapp.model.LoginPayload
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import okhttp3.Response
import java.util.regex.Pattern
import javax.security.auth.callback.Callback

class LoginActivity : AppCompatActivity(), OnClickListener {
    var login_btn_next: Button? = null
    var login_signUp: TextView? = null
    var login_emailNumber: EditText? = null

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
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
        login_btn_next?.setOnClickListener(this)
        login_signUp?.setOnClickListener(this)
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

    fun btLogin():Boolean {

        if (login_emailNumber?.length() == 0) {
            login_emailNumber?.setError(getString(R.string.valid_error));
            return false;
        }


        if (login_emailNumber?.text.toString().contains("@")){
                val str_email = login_emailNumber?.text.toString().trim()
                if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(str_email))) {
                    login_emailNumber?.setError(getString(R.string.email_error));
                    return false;

            }

            Toast.makeText(this, "Email", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PasswordActivity::class.java)
            startActivity(intent)
            return true
        }else{
            if(TextUtils.isDigitsOnly(login_emailNumber?.text.toString())){

                val str_number = login_emailNumber?.text.toString().trim()
                if (!(PHONE_NUMBER_PATTERN.toRegex().matches(str_number))) {
                    login_emailNumber?.setError(getString(R.string.phone_error));
                    return false;
                }
                Toast.makeText(this, "Number", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PasswordActivity::class.java)
                startActivity(intent)
                return true
            }
        }

      return false
    }


    fun btSignup() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }


}
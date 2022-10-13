package com.example.cryptoapp.modual.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cryptoapp.R
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity(),OnClickListener {


    var user_btn_email: Button? = null
    var user_btn_password: Button? = null
    var create_account: Button? = null
    var sp_et_email: EditText? = null
    var mn_et_phone: EditText? = null
    var sp_et_firstName: EditText? = null
    var sp_et_lastName: EditText? = null
    var sp_et_password: EditText? = null
    var sp_et_rePassword: EditText? = null
    var isUser: Boolean = false
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

    val PHONE_NUMBER_PATTERN = Pattern.compile(
        "[0-9]{10}"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()

    }

    fun init() {
        user_btn_email = findViewById(R.id.user_btn_email)
        user_btn_password = findViewById(R.id.user_btn_password)
        create_account = findViewById(R.id.create_account)
        sp_et_firstName =findViewById(R.id.sp_et_firstName)
        sp_et_lastName = findViewById(R.id.sp_et_lastName)
        sp_et_password = findViewById(R.id.sp_et_password)
        sp_et_password = findViewById(R.id.sp_et_password)
        sp_et_rePassword = findViewById(R.id.sp_et_rePassword)

        sp_et_email = findViewById(R.id.sp_et_email)
        mn_et_phone = findViewById(R.id.mn_et_phone)

        user_btn_password!!.setBackgroundColor(0)
        sp_et_email?.visibility = View.VISIBLE
        mn_et_phone?.visibility = View.GONE
        isUser = false

        user_btn_email!!.setOnClickListener(this)
        user_btn_password!!.setOnClickListener(this)
        create_account!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.user_btn_email -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this?.let { it1 -> user_btn_email!!.setBackgroundColor(it1.getColor(R.color.light_grey)) }
                }
                user_btn_password!!.setBackgroundColor(0)
                mn_et_phone?.visibility = View.GONE
                sp_et_email?.visibility = View.VISIBLE
                isUser = false
            }
            R.id.user_btn_password -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this?.getColor(R.color.light_grey)
                        ?.let { it1 -> user_btn_password!!.setBackgroundColor(it1) }
                }
                user_btn_email!!.setBackgroundColor(0)
                mn_et_phone?.visibility = View.VISIBLE
                sp_et_email?.visibility = View.GONE
                isUser = true
            }

            R.id.create_account -> {
                signUp(isUser)
            }
        }
    }


    fun signUp(isUser: Boolean): Boolean {

        if (sp_et_firstName?.length() == 0) {
            sp_et_firstName?.setError(getString(R.string.valid_error));
            return false;
        }

        if (sp_et_lastName?.length() == 0) {
            sp_et_lastName?.setError(getString(R.string.valid_error));
            return false;
        }

        if (isUser == false) {

            if (sp_et_email?.length() == 0) {
                sp_et_email?.setError(getString(R.string.valid_error));
                return false;
            }else{

                val str_email = sp_et_email?.text.toString().trim()
                if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(str_email))) {
                    sp_et_email?.setError(getString(R.string.email_error));
                    return false;
                }
            }


        } else {

            if (mn_et_phone?.length() == 0) {
                mn_et_phone?.setError(getString(R.string.valid_error));
                return false;
            }

            val str_number = mn_et_phone?.text.toString().trim()
            if (!(PHONE_NUMBER_PATTERN.toRegex().matches(str_number))) {
                mn_et_phone?.setError(getString(R.string.phone_error));
                return false;
            }
        }

        if (sp_et_password?.length() == 0) {
            sp_et_password?.setError(getString(R.string.password_error));
            return false;
        }

        if (sp_et_rePassword?.length() == 0) {
            sp_et_rePassword?.setError(getString(R.string.repassword_error));
            return false;
        }



        val intent = Intent(this, OtpActivity::class.java)
        if (isUser == false) {
            intent.putExtra("register", sp_et_email?.text.toString())

        }else{

            intent.putExtra("register", mn_et_phone?.text.toString())
        }
        intent.putExtra("isRegister", true)
        startActivity(intent)
        return true
        }


    }
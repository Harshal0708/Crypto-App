package com.example.cryptoapp.modual.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.widget.*
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.RegisterResponse
import com.example.cryptoapp.model.RegisterPayload
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.google.gson.Gson
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity(), OnClickListener {


    lateinit var sp_et_email: EditText
    lateinit var mn_et_phone: EditText
    lateinit var sp_et_firstName: EditText
    lateinit var sp_et_lastName: EditText
    lateinit var sp_et_password: EditText
    lateinit var sp_et_rePassword: EditText

    lateinit var view: View
    lateinit var register_progressBar: ProgressBar
    lateinit var resent: TextView
    lateinit var progressBar_cardView: RelativeLayout

    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var firsName: String
    private lateinit var lastName: String
    private lateinit var password: String
    private lateinit var rePassword: String

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

    val PASSWORD = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()

    }

    fun init() {

        sp_et_firstName = findViewById(R.id.sp_et_firstName)
        sp_et_lastName = findViewById(R.id.sp_et_lastName)
        sp_et_password = findViewById(R.id.sp_et_password)
        sp_et_rePassword = findViewById(R.id.sp_et_rePassword)

        sp_et_email = findViewById(R.id.sp_et_email)
        mn_et_phone = findViewById(R.id.mn_et_phone)

        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)

        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)
        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.create_account)
        progressBar_cardView.setOnClickListener(this)


        sp_et_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var pwd = sp_et_password.text.toString().trim()

                if (!(PASSWORD.toRegex().matches(pwd))) {
                    sp_et_password.setError(getString(R.string.valid_password))
                }else{
                    Toast.makeText(this@RegisterActivity,"Password Verify Done!",Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


        sp_et_rePassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var pwd = sp_et_rePassword.text.toString().trim()


                if (!(PASSWORD.toRegex().matches(pwd))) {
                    sp_et_rePassword.setError(getString(R.string.valid_password))
                }else{
                    Toast.makeText(this@RegisterActivity,"Repassword Verify Done!",Toast.LENGTH_SHORT).show()
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

                email = sp_et_email.text.toString()
                phone = mn_et_phone.text.toString()
                firsName = sp_et_firstName.text.toString()
                lastName = sp_et_lastName.text.toString()
                password = sp_et_password.text.toString()
                rePassword = sp_et_rePassword.text.toString()

                if (validation() == true) {
                   // Toast.makeText(this@RegisterActivity,"Register",Toast.LENGTH_SHORT).show()
                addCreateAccount()
                }
            }
        }
    }

    fun addCreateAccount() {

        register_progressBar?.visibility = View.VISIBLE
        val response = ServiceBuilder.buildService(RestApi::class.java)

        //val payload = RegisterPayload(password,rePassword,email,firsName,lastName,"","","","","",phone,"Appu25")
        val payload = RegisterPayload(
            email,
            firsName,
            lastName,
            rePassword,
            email,
            "",
            "",
            "",
            "",
            "",
            phone,
            false,
            false,
            false
        )
        val gson = Gson()
        val json = gson.toJson(payload)
        Log.d("test", json)
        response.addRegister(payload)
            .enqueue(
                object : retrofit2.Callback<RegisterResponse> {
                    override fun onResponse(
                        call: retrofit2.Call<RegisterResponse>,
                        response: retrofit2.Response<RegisterResponse>
                    ) {

                        Log.d("test", response.toString())
                        Log.d("test", response.body().toString())

                        if (response.body()?.code == "200") {
                            register_progressBar?.visibility = GONE
                            Toast.makeText(
                                this@RegisterActivity,
                                response.body()?.message,
                                Toast.LENGTH_LONG
                            ).show()


                            val intent = Intent(this@RegisterActivity, OtpActivity::class.java)
                            intent.putExtra("email",  response.body()?.data?.email)
                            intent.putExtra("phone", response.body()?.data?.mobile_No)
                            intent.putExtra("emailOtp", response.body()?.data?.email_OTP)
                            intent.putExtra("mobileOtp", response.body()?.data?.mobile_OTP)
                            startActivity(intent)


                        } else {

                            register_progressBar?.visibility = GONE
                            Toast.makeText(
                                this@RegisterActivity,
                                "User not created!",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }

                    override fun onFailure(call: retrofit2.Call<RegisterResponse>, t: Throwable) {
                        Log.d("test", t.toString())

                        register_progressBar?.visibility = GONE
                        Toast.makeText(this@RegisterActivity, t.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                }
            )


    }

    fun validation(): Boolean {

        if (sp_et_firstName.length() == 0) {
            sp_et_firstName.setError(getString(R.string.valid_error));
            return false;
        }

        if (sp_et_lastName.length() == 0) {
            sp_et_lastName.setError(getString(R.string.valid_error));
            return false;
        }

        if (sp_et_email.length() == 0) {
            sp_et_email.setError(getString(R.string.valid_error));
            return false;
        }

        email = sp_et_email.text.toString().trim()
        if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(email))) {
            sp_et_email.setError(getString(R.string.email_error));
            return false;
        }

        if (mn_et_phone.length() == 0) {
            mn_et_phone.setError(getString(R.string.valid_error));
            return false;
        }

        phone = mn_et_phone.text.toString().trim()
        if (!(PHONE_NUMBER_PATTERN.toRegex().matches(phone))) {
            mn_et_phone.setError(getString(R.string.phone_error));
            return false;
        }


        if (sp_et_password.length() == 0) {
            sp_et_password.setError(getString(R.string.password_error));
            return false;
        }

        if (sp_et_rePassword.length() == 0) {
            sp_et_rePassword.setError(getString(R.string.repassword_error));
            return false;
        }

        if (!sp_et_password.text.toString().equals(sp_et_rePassword.text.toString())) {
            sp_et_rePassword.setError(getString(R.string.password_not_error));
            return false;
        }

        return true
    }

}



package com.example.cryptoapp.modual.login

import android.app.Dialog
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.GetCountriesResponseItem
import com.example.cryptoapp.Response.LoginResponse
import com.example.cryptoapp.model.LoginPayload
import com.example.cryptoapp.modual.authenticator.GoogleAuthenticatorActivity
import com.example.cryptoapp.modual.countries.CountriesAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.network.onItemClickListener
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity(), OnClickListener, OnTouchListener, onItemClickListener {

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
    lateinit var cb_remember_me: CheckBox
    lateinit var txt_login_country_code: TextView

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

    lateinit var preferences: MyPreferences
    var passwordVisiable = true

    lateinit var countriesAdapter: CountriesAdapter
    lateinit var rv_countryName: RecyclerView
    lateinit var viewLoader: View
    lateinit var toolbar: View
    lateinit var toolbar_img_back: ImageView
    lateinit var animationView: LottieAnimationView
    var countryId: String = ""
    lateinit var getCountriesResponseItem: ArrayList<GetCountriesResponseItem>
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    fun init() {
        preferences = MyPreferences(this)

        login_signUp = findViewById(R.id.txt_sign_in_here)
        login_emailNumber = findViewById(R.id.login_emailNumber)
        pwd_password = findViewById(R.id.pwd_password)
        cb_remember_me = findViewById(R.id.cb_remember_me)
        login_signUp.setOnClickListener(this)

        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)

        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)
        forgot_password = findViewById(R.id.forgot_password)
        login_create = findViewById(R.id.txt_otp_resend)
        txt_login_country_code = findViewById(R.id.txt_login_country_code)

        register_progressBar.visibility = GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.sign_in_login)
        progressBar_cardView.setOnClickListener(this)
        txt_login_country_code.setOnClickListener(this)

        forgot_password.setOnClickListener(this)
        login_create.setOnClickListener(this)
        pwd_password.setOnTouchListener(this)

        login_emailNumber.setBackground(getResources().getDrawable(R.drawable.edt_bg_normal))
        pwd_password.setBackground(getResources().getDrawable(R.drawable.edt_bg_normal))

        login_emailNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var pwd = login_emailNumber.text.toString().trim()

                if (login_emailNumber.length() > 0) {
                    login_emailNumber.setBackground(getResources().getDrawable(R.drawable.edt_bg_selected))
                } else {
                    login_emailNumber.setBackground(getResources().getDrawable(R.drawable.edt_bg_normal))
                }


                if (pwd.contains("@")) {
                    if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(pwd))) {
                        txt_login_country_code.visibility = GONE

                        login_emailNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            R.drawable.ic_new_email, 0, 0, 0
                        )

                    }
                } else {

                    if (TextUtils.isDigitsOnly(pwd)) {
                        if (!(PHONE_NUMBER_PATTERN.toRegex().matches(pwd))) {
                            txt_login_country_code.visibility = VISIBLE
                            login_emailNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0, 0, 0, 0
                            )
                        }
                    } else if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(pwd))) {
                        txt_login_country_code.visibility = GONE
                        login_emailNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            R.drawable.ic_new_email, 0, 0, 0
                        )
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        pwd_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var pwd = pwd_password.text.toString().trim()

                if (pwd_password.length() > 0) {
                    pwd_password.setBackground(getResources().getDrawable(R.drawable.edt_bg_selected))
                } else {
                    pwd_password.setBackground(getResources().getDrawable(R.drawable.edt_bg_normal));
                }


                if (!(PASSWORD.toRegex().matches(pwd))) {
                    if (pwd.length > 5) {
                        showToast(this@LoginActivity, getString(R.string.valid_password))
                    }
                } else {
                    showToast(this@LoginActivity, getString(R.string.password_verify_done))
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
            R.id.txt_login_country_code -> {
                exit()
            }
            R.id.toolbar_img_back -> {
                dialog.dismiss()
            }

        }
    }

    fun exit() {
        dialog = Dialog(this, android.R.style.ThemeOverlay)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.getWindow()?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_countries)
        viewLoader = dialog.findViewById(R.id.viewLoader)
        toolbar = dialog.findViewById(R.id.toolbar)
        toolbar_img_back = toolbar.findViewById(R.id.toolbar_img_back)
        toolbar_img_back.setOnClickListener(this)
        animationView = viewLoader.findViewById(R.id.lotti_img)
        rv_countryName = dialog.findViewById(R.id.rv_countryName)
        setupAnim()
        getCountries()
        dialog.show()
    }

    private fun setupAnim() {
        animationView.setAnimation(R.raw.currency)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    private fun getCountries() {
        viewLoader.visibility = VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var response = ServiceBuilder(this@LoginActivity).buildService(RestApi::class.java)
                .getCountries()
            withContext(Dispatchers.Main) {
                viewLoader.visibility = GONE
                getCountriesResponseItem = response.body()!!
                rv_countryName.layoutManager = LinearLayoutManager(this@LoginActivity)
                countriesAdapter = CountriesAdapter(
                    this@LoginActivity,
                    getCountriesResponseItem,
                    LoginActivity(),
                    this@LoginActivity
                )
                rv_countryName.adapter = countriesAdapter
            }
        }
    }

    private fun addLogin(email: String) {
        register_progressBar.visibility = View.VISIBLE

        val response = ServiceBuilder(this@LoginActivity).buildService(RestApi::class.java)

        var str_email = ""
        var str_mobile = ""

        if (isMobile == false) {
            str_email = email
        } else {
            str_mobile = email
        }

        val payload = LoginPayload(
            str_email, password, str_mobile, false,countryId
        )

//        val payload = LoginPayload(
//            "apurva.skyttus@gmail.com", "Test@123", "9714675391", false
//        )

        response.addLogin(payload).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(
                call: retrofit2.Call<LoginResponse>, response: retrofit2.Response<LoginResponse>
            ) {
                showToast(this@LoginActivity, response.body()?.message.toString())
                register_progressBar.visibility = GONE
                if (response.body()?.isSuccess == true) {
                    var intent = Intent(this@LoginActivity, GoogleAuthenticatorActivity::class.java)
                    intent.putExtra("data", Gson().toJson(response.body()?.data))
                    intent.putExtra("isChecked", cb_remember_me.isChecked)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                register_progressBar.visibility = GONE
                showToast(this@LoginActivity, getString(R.string.login_failed))
            }

        })

    }

    fun btLogin(): Boolean {

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
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        val Right = 2
        if (p1?.action == MotionEvent.ACTION_UP) {
            if (p1.getRawX() >= pwd_password?.right!!.minus(
                    pwd_password.compoundDrawables[Right].getBounds().width()
                )
            ) {
                var selecion = pwd_password.selectionEnd

                if (passwordVisiable) {
                    pwd_password.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_new_email, 0, R.drawable.ic_eye, 0
                    )
                    pwd_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                    passwordVisiable = false
                } else {
                    pwd_password.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_new_email, 0, R.drawable.ic_eye_off, 0
                    )
                    pwd_password.setTransformationMethod(PasswordTransformationMethod.getInstance())
                    passwordVisiable = true
                }

                pwd_password.setSelection(selecion)

                return true
            }
        }

        return false
    }

    override fun onItemClick(pos: Int) {
        txt_login_country_code.text = "+ ${getCountriesResponseItem.get(pos).countryCode}"
        countryId = getCountriesResponseItem.get(pos).id
        dialog.dismiss()
    }
}
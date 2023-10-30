package com.example.cryptoapp.modual.login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyManager
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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.Constants.Companion.showLog
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
import com.example.cryptoapp.singleton.MySingleton
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
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

    lateinit var animationView: LottieAnimationView
    var countryId: String = ""
    lateinit var getCountriesResponseItem: ArrayList<GetCountriesResponseItem>
    lateinit var dialog: Dialog

    private lateinit var ima_back: ImageView

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


        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)
        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)

        forgot_password = findViewById(R.id.forgot_password)
        login_create = findViewById(R.id.txt_otp_resend)
        txt_login_country_code = findViewById(R.id.txt_login_country_code)

        register_progressBar.visibility = GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.sign_in_login)


        login_signUp.setOnClickListener(this)
        progressBar_cardView.setOnClickListener(this)
        txt_login_country_code.setOnClickListener(this)
        forgot_password.setOnClickListener(this)
        login_create.setOnClickListener(this)
        pwd_password.setOnTouchListener(this)

        login_emailNumber.setBackground(getResources().getDrawable(R.drawable.edt_bg_normal))
        pwd_password.setBackground(getResources().getDrawable(R.drawable.edt_bg_normal))

        countryId=GetCountryZipCode()!!
        txt_login_country_code.text ="+${countryId}"

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
                        showToast(this@LoginActivity,this@LoginActivity, getString(R.string.valid_password))
                    }
                } else {
                    showToast(this@LoginActivity, this@LoginActivity,getString(R.string.password_verify_done))
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }


    fun GetCountryZipCode(): String? {
        var CountryID = ""
        var CountryZipCode = ""
        val manager = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        //getNetworkCountryIso
        CountryID = manager.simCountryIso.uppercase(Locale.getDefault())
        val rl = this.resources.getStringArray(R.array.CountryCodes)
        for (i in rl.indices) {
            val g = rl[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            if (g[1].trim { it <= ' ' } == CountryID.trim { it <= ' ' }) {
                CountryZipCode = g[0]
                break
            }
        }
        return CountryZipCode
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

            R.id.ima_back -> {
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
        ima_back = dialog.findViewById(R.id.ima_back)

        ima_back.setOnClickListener(this)
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
            var response = ServiceBuilder(this@LoginActivity,false).buildService(RestApi::class.java)
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

        val response = ServiceBuilder(this@LoginActivity,false).buildService(RestApi::class.java)

        var str_email = ""
        var str_mobile = ""

        if (isMobile == false) {
            str_email = email
        } else {
            str_mobile = email
        }

        val payload = LoginPayload(
            str_email, password, str_mobile,  countryId
        )
//
//
//        val payload = LoginPayload(
//            "apurva.skyttus@gmail.com", "Test@123", "9714675391", false,"973e68ae-1963-430d-2d8f-08db88dc0d87"
//        )


        response.addLogin(payload).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(
                call: retrofit2.Call<LoginResponse>, response: retrofit2.Response<LoginResponse>
            ) {

                showToast(this@LoginActivity,this@LoginActivity, response.body()?.message.toString())
                register_progressBar.visibility = GONE

                if (response.body()?.isSuccess == true) {

                    showLog("Login data",response.body()!!.data.toString())
                    MySingleton().setData(response.body()!!.data)

                    if(MyPreferences(this@LoginActivity).getAuth() == 0){
                        var intent = Intent(this@LoginActivity, GoogleAuthenticatorActivity::class.java)
                        //intent.putExtra("data", response.body()!!.data);
                        intent.putExtra("isChecked", cb_remember_me.isChecked)
                        startActivity(intent)
                    }else if(MyPreferences(this@LoginActivity).getAuth() == 1){
                        if (preferences.getEnable() == false) {
                            val intent = Intent(this@LoginActivity, BiometricEnableActivity::class.java)
//                            intent.putExtra("data", Gson().toJson(response.body()!!.data))
                            intent.putExtra("isChecked", cb_remember_me.isChecked)
                            startActivity(intent)
                            finish()
                        } else {
                            val intent = Intent(this@LoginActivity, BiometricActivity::class.java)
//                            intent.putExtra("data", Gson().toJson(response.body()!!.data))
                            intent.putExtra("isChecked", cb_remember_me.isChecked)
                            startActivity(intent)
                            finish()
                        }
                    }else if(MyPreferences(this@LoginActivity).getAuth() == 2){
                        var intent = Intent(this@LoginActivity, LoginOtpActivity::class.java)
                        //intent.putExtra("data", response.body()!!.data);
//                        intent.putExtra("data", Gson().toJson(response.body()!!.data))
                        intent.putExtra("isChecked", cb_remember_me.isChecked)
                        startActivity(intent)
                    }else{
                        var intent = Intent(this@LoginActivity, GoogleAuthenticatorActivity::class.java)
                        //intent.putExtra("data", response.body()!!.data);
//                        intent.putExtra("data", Gson().toJson(response.body()!!.data))
                        intent.putExtra("isChecked", cb_remember_me.isChecked)
                        startActivity(intent)
                    }

                }

//                val dataXX = ArrayList<DataXX>()
//                dataXX.add(response.body()!!.data)
//
//                val imageBytes = Base64.decode(response.body()!!.data.profilePicture, 0)
//
//                val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//
//                val myData= DataXX(
//                    response.body()!!.data.accessToken,
//                    response.body()!!.data.email,
//                    response.body()!!.data.firstName,
//                    response.body()!!.data.isKycVerify,
//                    response.body()!!.data.lastName,
//                    response.body()!!.data.mobile,
//                    response.body()!!.data.name,
//                    "",
//                    response.body()!!.data.refreshToken,
//                    response.body()!!.data.refreshTokenExpiryTime,
//                    response.body()!!.data.userId,
//                    response.body()!!.data.countryId,
//                    response.body()!!.data.haveAnySubscription,
//                    response.body()!!.data.countryCode
//
//                    )
//                val gson = Gson()
//                val jsonData = gson.toJson(myData)
//
//
//                var intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
//                intent.putExtra("data", jsonData)
//                intent.putExtra("isChecked", cb_remember_me.isChecked)
//                startActivity(intent)
                

//                val gson = Gson()
//                val jsonData = gson.toJson(myData)


//                var intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
//                intent.putExtra("data", Gson().toJson(response.body()!!.data.profilePicture))
//                intent.putExtra("isChecked", cb_remember_me.isChecked)
//                startActivity(intent)
//

            }

            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                register_progressBar.visibility = GONE
                showToast(this@LoginActivity,this@LoginActivity, getString(R.string.login_failed))
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
            if (p1.getRawX() >= pwd_password.right.minus(
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
        countryId = getCountriesResponseItem.get(pos).countryCode.toString()
        dialog.dismiss()
    }

}

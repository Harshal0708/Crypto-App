package com.example.cryptoapp.modual.login

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.example.cryptoapp.Receiver.SmsBroadcastReceiver
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.Response.SendRegistrationOtpResponce
import com.example.cryptoapp.model.SendLoginOtpPayload
import com.example.cryptoapp.model.VerifyLoginOtpPayload
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.gson.Gson
import retrofit2.Call
import java.util.regex.Pattern


class LoginOtpActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var preferences: MyPreferences
    lateinit var view: View
    lateinit var otp_layout: View
    lateinit var register_progressBar: ProgressBar
    lateinit var resent: TextView
    lateinit var progressBar_cardView: RelativeLayout

    lateinit var otp_phone_verification: TextView
    val REQ_USER_CONSENT = 200
    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver

    lateinit var data: DataXX

    lateinit var timer: CountDownTimer

    lateinit var animationView: LottieAnimationView
    lateinit var otp_1: EditText
    lateinit var otp_2: EditText
    lateinit var otp_3: EditText
    lateinit var otp_4: EditText
    lateinit var otp_5: EditText
    lateinit var otp_6: EditText

    lateinit var resend_timer: TextView

    lateinit var resend_code: TextView
    lateinit var txt_otp_resend: TextView

    lateinit var generateOtp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_otp)
        init()

    }

    fun init() {
        preferences = MyPreferences(this)
        resend_code = findViewById(R.id.txt_sign_in_here)
        txt_otp_resend = findViewById(R.id.txt_otp_resend)

        otp_phone_verification = findViewById(R.id.otp_phone_verification)
        otp_layout = findViewById(R.id.otp_layout)
        animationView = findViewById(R.id.login_img)
        setupAnim()
        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)
        resend_timer = findViewById(R.id.resend_timer)

        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)
        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.submit)
        resent.text = getString(R.string.verify_continue)
        data = Gson().fromJson(intent.getStringExtra("data"), DataXX::class.java)

        otp_phone_verification.setText("Please, enter the verification code we sent to your  Mobile ${data.mobile} and Gmail ${data.email}")

        countdownTimer()

        progressBar_cardView.setOnClickListener(this)
        otp_1 = otp_layout.findViewById(R.id.otp_1)
        otp_2 = otp_layout.findViewById(R.id.otp_2)
        otp_3 = otp_layout.findViewById(R.id.otp_3)
        otp_4 = otp_layout.findViewById(R.id.otp_4)
        otp_5 = otp_layout.findViewById(R.id.otp_5)
        otp_6 = otp_layout.findViewById(R.id.otp_6)
        resend_code.isEnabled = false
        txt_otp_resend.isEnabled = false
        resend_code.setOnClickListener(this)
        txt_otp_resend.setOnClickListener(this)

        otp_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length!! > 0) {
                    showkeybord(otp_2, otp_1, false)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        otp_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (p0?.length!! > 0) {
                    showkeybord(otp_3, otp_2, false)
                } else {
                    showkeybord(otp_1, otp_2, true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        otp_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length!! > 0) {
                    showkeybord(otp_4, otp_3, false)
                } else {
                    showkeybord(otp_2, otp_3, true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        otp_4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length!! > 0) {
                    showkeybord(otp_5, otp_4, false)
                } else {
                    showkeybord(otp_3, otp_4, true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        otp_5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length!! > 0) {
                    showkeybord(otp_6, otp_5, false)
                } else {
                    showkeybord(otp_4, otp_5, true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        otp_6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length!! > 0) {
                } else {
                    showkeybord(otp_5, otp_6, true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }

    fun sendLoginOtp(mobile: String?, email: String?) {
        otp_1.text.clear()
        otp_2.text.clear()
        otp_3.text.clear()
        otp_4.text.clear()
        otp_5.text.clear()
        otp_6.text.clear()

        register_progressBar?.visibility = View.VISIBLE
        val response = ServiceBuilder(this@LoginOtpActivity).buildService(RestApi::class.java)

        val payload = SendLoginOtpPayload(
            email!!,
            mobile!!
        )

        response.addSendLoginOtp(payload)
            .enqueue(
                object : retrofit2.Callback<SendRegistrationOtpResponce> {
                    override fun onResponse(
                        call: Call<SendRegistrationOtpResponce>,
                        response: retrofit2.Response<SendRegistrationOtpResponce>
                    ) {
                        if (response.body()?.isSuccess == true) {
                            register_progressBar.visibility = View.GONE
                            response.body()?.message?.let { showToast(this@LoginOtpActivity, it) }
                        }
                    }

                    override fun onFailure(
                        call: Call<SendRegistrationOtpResponce>,
                        t: Throwable
                    ) {
                        register_progressBar.visibility = View.GONE
                        showToast(this@LoginOtpActivity, getString(R.string.otp_failed))
                    }
                }
            )
    }


    private fun countdownTimer() {
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(reming: Long) {

                resend_code.isEnabled = false
                txt_otp_resend.isEnabled = false
                var remainingTime = 60
                remainingTime = (reming / 1000).toInt();
                resend_timer.text = getString(R.string.resend_in) + remainingTime.toString()
            }

            override fun onFinish() {
                resend_timer.visibility = View.GONE
                //  resend_timer.text = "Done!"
                resend_code.isEnabled = true
                txt_otp_resend.isEnabled = true
            }
        }

        timer.start()
    }

    private fun showkeybord(one: EditText?, two: EditText?, isBoolean: Boolean) {

        one?.setFocusable(true)
        one?.setFocusableInTouchMode(true)
        one?.requestFocus()
        two?.setFocusable(false)
        two?.setFocusableInTouchMode(false)
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(one, InputMethodManager.SHOW_IMPLICIT)

    }

    private fun setupAnim() {
        animationView.setAnimation(R.raw.verified)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    private fun startSmartUserConsent() {
        val client = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)
    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver.smsBroadcastReceiverListener =
            object : SmsBroadcastReceiver.SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent) {
                    startActivityForResult(intent, REQ_USER_CONSENT)
                }

                override fun onFailure() {
                    TODO("Not yet implemented")
                }

            }

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_USER_CONSENT) {
            if (resultCode == RESULT_OK && data != null) {
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                getOtpFromMessage(message)
            }
        }
    }

    private fun getOtpFromMessage(message: String?) {
        val otpPatter = Pattern.compile("(|^)\\d{6}")
        val matcher = otpPatter.matcher(message)
        if (matcher.find()) {
            //   et_phone_otp.setText(matcher.group(0))
        }
    }

    override fun onStart() {
        super.onStart()
        //registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        //unregisterReceiver(smsBroadcastReceiver)
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.progressBar_cardView -> {

                generateOtp = otp_1.text.toString() +
                        otp_2.text.toString() +
                        otp_3.text.toString() +
                        otp_4.text.toString() +
                        otp_5.text.toString() +
                        otp_6.text.toString()

                verifyRegistrationOtp(generateOtp)
            }
            R.id.txt_sign_in_here -> {
                resend()
            }
            R.id.txt_otp_resend -> {
                resend()
            }
        }
    }

    fun resend() {
        resend_timer.visibility = View.VISIBLE
        sendLoginOtp(data.mobile, data.email)
        timer.start()
    }


    fun verifyRegistrationOtp(otp: String) {

        register_progressBar.visibility = View.VISIBLE
        val response = ServiceBuilder(this@LoginOtpActivity).buildService(RestApi::class.java)

        val payload = VerifyLoginOtpPayload(
            data.mobile,
            otp
        )

        response.addVerifyLoginOtp(payload)
            .enqueue(
                object : retrofit2.Callback<SendRegistrationOtpResponce> {
                    override fun onResponse(
                        call: Call<SendRegistrationOtpResponce>,
                        response: retrofit2.Response<SendRegistrationOtpResponce>
                    ) {
                        if (response.body()?.isSuccess == true) {
                            register_progressBar.visibility = View.GONE
                            if (intent.getBooleanExtra("isChecked", false) == true) {
                                preferences.setRemember(true)
                            } else {
                                preferences.setRemember(false)
                            }
                            preferences.setLogin(data)
                            preferences.setToken(data.accessToken)

                            var intent = Intent(this@LoginOtpActivity, UserActivity::class.java)
                            startActivity(intent)
                            finish()
                            response.body()?.message?.let { showToast(this@LoginOtpActivity, it) }
                        } else {
                            register_progressBar.visibility = View.GONE
                            response.body()?.message?.let { showToast(this@LoginOtpActivity, it) }
                        }
                    }

                    override fun onFailure(call: Call<SendRegistrationOtpResponce>, t: Throwable) {
                        register_progressBar.visibility = View.GONE
                        showToast(this@LoginOtpActivity, getString(R.string.otp_failed))
                    }

                }
            )


    }

    fun validation(): Boolean {

//        if (et_phone_otp.length() == 0) {
//            et_phone_otp.setError(getString(R.string.valid_error));
//            return false
//        }

        return true
    }
}
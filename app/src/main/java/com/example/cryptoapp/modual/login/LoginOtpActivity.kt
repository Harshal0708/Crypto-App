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
import com.example.cryptoapp.R
import com.example.cryptoapp.Receiver.SmsBroadcastReceiver
import com.google.android.gms.auth.api.phone.SmsRetriever
import java.util.regex.Pattern


class LoginOtpActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var view: View
    lateinit var otp_layout: View
    lateinit var register_progressBar: ProgressBar
    lateinit var resent: TextView
    lateinit var progressBar_cardView: RelativeLayout

    lateinit var otp_phone_verification: TextView
    val REQ_USER_CONSENT = 200
    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver

    var phone_otp: String = ""
    var phone: String = ""
    var email: String = ""
    lateinit var str_phone_otp: String
    lateinit var timer: CountDownTimer

    lateinit var animationView: LottieAnimationView
    lateinit var otp_1: EditText
    lateinit var otp_2: EditText
    lateinit var otp_3: EditText
    lateinit var otp_4: EditText
    lateinit var otp_5: EditText
    lateinit var otp_6: EditText

    var selectedKeyPos: Int = 0

    lateinit var generateOtp: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_otp)
        init()

    }

    fun init() {

        otp_phone_verification = findViewById(R.id.otp_phone_verification)
        otp_layout = findViewById(R.id.otp_layout)
        animationView = findViewById(R.id.login_img)
        setupAnim()
        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)

        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)
        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.submit)
        resent.text = getString(R.string.verify_continue)
        phone = intent.getStringExtra("phone").toString()
        email = intent.getStringExtra("email").toString()
//        otp_phone_verification.setText(phone)
//        otp_email_verification.setText(email)
        otp_phone_verification.setText("Please, enter the verification code we sent to your  Mobile ${phone} and Gmail ${email}")

        phone_otp = intent.getStringExtra("mobileOtp").toString()

        progressBar_cardView.setOnClickListener(this)
        otp_1 = otp_layout.findViewById(R.id.otp_1)
        otp_2 = otp_layout.findViewById(R.id.otp_2)
        otp_3 = otp_layout.findViewById(R.id.otp_3)
        otp_4 = otp_layout.findViewById(R.id.otp_4)
        otp_5 = otp_layout.findViewById(R.id.otp_5)
        otp_6 = otp_layout.findViewById(R.id.otp_6)


        otp_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length!! > 0) {
                    showkeybord(otp_2, otp_1, false)

                } else {
                    Toast.makeText(this@LoginOtpActivity, "Clear", Toast.LENGTH_SHORT).show()
                }
            }

        })

        otp_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length!! > 0) {
                    showkeybord(otp_3, otp_2, false)
                } else {
                    showkeybord(otp_1, otp_2, true)
                }
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

                    Toast.makeText(this@LoginOtpActivity, "Done", Toast.LENGTH_SHORT).show()
                } else {
                    showkeybord(otp_5, otp_6, true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }

    private fun showkeybord(one: EditText?, two: EditText?, isBoolean: Boolean) {


            one?.setFocusable(true);
            one?.setFocusableInTouchMode(true);
            one?.requestFocus();
            two?.setFocusable(false);
            two?.setFocusableInTouchMode(false);
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

                if (generateOtp == phone_otp) {

                    var intent = Intent(this@LoginOtpActivity, UserActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Not Done",
                        Toast.LENGTH_SHORT
                    ).show()
                    //Toast.makeText(this, "Email or Mobile Number not match!", Toast.LENGTH_SHORT).show()
                }


//                if (validation() == true) {
//                    //  addOtp()
//                    var intent = Intent(this@LoginOtpActivity, UserActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }

            }
        }
    }

//
//    fun addOtp() {
//        Log.d("test", str_phone_otp + "")
//        Log.d("test", str_email_otp + "")
//        var intent = Intent(this@OtpActivity, LoginActivity::class.java)
//        startActivity(intent)
//    }


//    fun addOtp() {
//
//        register_progressBar?.visibility = View.VISIBLE
//        val response = ServiceBuilder.buildService(RestApi::class.java)
//
//        //val payload = RegisterPayload(password,rePassword,email,firsName,lastName,"","","","","",phone,"Appu25")
//        val payload = OtpPayload(
//            str_phone_otp.toString(),
//            str_email_otp.toString(),
//            email,
//            phone
//        )
//        val gson = Gson()
//        val json = gson.toJson(payload)
//        Log.d("test", json)
//        response.addOtp( str_phone_otp.toString(), str_email_otp.toString(),email,phone)
//            .enqueue(
//                object : retrofit2.Callback<OtpResponse> {
//                    override fun onResponse(
//                        call: Call<OtpResponse>,
//                        response: retrofit2.Response<OtpResponse>
//                    ) {
//
//                        Log.d("test", response.toString())
//                        Log.d("test", response.body().toString())
//
//                        if (response.body()?.code == "200") {
//                            register_progressBar?.visibility = View.GONE
//                            Toast.makeText(
//                                this@LoginOtpActivity,
//                                response.body()?.message,
//                                Toast.LENGTH_LONG
//                            ).show()
//
////                            Log.d("test", str_phone_otp + "")
////                            Log.d("test", str_email_otp + "")
//                            var intent = Intent(this@LoginOtpActivity, LoginActivity::class.java)
//                            startActivity(intent)
//
//                        } else {
//
//                            register_progressBar?.visibility = View.GONE
//                            Toast.makeText(
//                                this@LoginOtpActivity,
//                                "User not created!",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//
//                    }
//
//
//                    override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
//                        Log.d("test", t.toString())
//
//                        register_progressBar?.visibility = View.GONE
//                        Toast.makeText(this@LoginOtpActivity, t.toString(), Toast.LENGTH_LONG)
//                            .show()
//                    }
//
//                }
//            )
//
//
//    }
//    fun addResendOtp() {
//
//        register_progressBar?.visibility = View.VISIBLE
//        val response = ServiceBuilder.buildService(RestApi::class.java)
//
//        response.addResendOtp(email,phone)
//            .enqueue(
//                object : retrofit2.Callback<OtpResendResponse> {
//                    override fun onResponse(
//                        call: Call<OtpResendResponse>,
//                        response: retrofit2.Response<OtpResendResponse>
//                    ) {
//
//                        Log.d("test", response.toString())
//                        Log.d("test", response.body().toString())
//
//                        if (response.body()?.code == "200") {
//                            register_progressBar?.visibility = View.GONE
//
//
////                phone_otp = "123123"
////                email_otp = "123123"
//
//                            Toast.makeText(
//                                this@LoginOtpActivity,
//                                response.body()?.message,
//                                Toast.LENGTH_LONG
//                            ).show()
//
//                        } else {
//
//                            register_progressBar?.visibility = View.GONE
//                            Toast.makeText(
//                                this@LoginOtpActivity,
//                                "User not created!",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//
//                    }
//
//
//                    override fun onFailure(call: Call<OtpResendResponse>, t: Throwable) {
//                        Log.d("test", t.toString())
//
//                        register_progressBar?.visibility = View.GONE
//                        Toast.makeText(this@LoginOtpActivity, t.toString(), Toast.LENGTH_LONG)
//                            .show()
//                    }
//
//                }
//            )
//
//
//    }

    fun validation(): Boolean {

//        if (et_phone_otp.length() == 0) {
//            et_phone_otp.setError(getString(R.string.valid_error));
//            return false
//        }

        return true
    }
}
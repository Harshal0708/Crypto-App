package com.example.cryptoapp.modual.login

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.example.cryptoapp.R
import com.example.cryptoapp.Receiver.SmsBroadcastReceiver
import com.example.cryptoapp.Response.OtpResendResponse
import com.example.cryptoapp.Response.OtpResponse
import com.example.cryptoapp.model.OtpPayload
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.gson.Gson
import retrofit2.Call
import java.util.regex.Pattern

class LoginOtpActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var view: View
    lateinit var register_progressBar: ProgressBar
    lateinit var resent: TextView
    lateinit var progressBar_cardView: RelativeLayout

    lateinit var otp_phone_verification: TextView
    lateinit var otp_email_verification: TextView
    lateinit var et_phone_otp: EditText
    val REQ_USER_CONSENT = 200
    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver

    var phone_otp: String = ""
    var phone: String = ""
    var email: String = ""
    lateinit var str_phone_otp: String
    lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_otp)
        init()

    }

    fun init() {

        et_phone_otp = findViewById(R.id.et_phone_otp)
        otp_phone_verification = findViewById(R.id.otp_phone_verification)
        otp_email_verification = findViewById(R.id.otp_email_verification)

        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)

        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)
        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.submit)

        phone = intent.getStringExtra("phone").toString()
        email = intent.getStringExtra("email").toString()
        otp_phone_verification.setText(phone)
        otp_email_verification.setText(email)
        phone_otp = intent.getStringExtra("mobileOtp").toString()

        progressBar_cardView.setOnClickListener(this)

        et_phone_otp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var otp = et_phone_otp.text.toString().trim()

                if (otp != phone_otp) {
                    et_phone_otp.setError(getString(R.string.valid_otp));
                } else {
                    str_phone_otp = otp
                    Toast.makeText(
                        this@LoginOtpActivity,
                        getString(R.string.otp_verify_done),
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        //  startSmartUserConsent()
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
            et_phone_otp.setText(matcher.group(0))
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

                if (validation() == true) {
                    //  addOtp()
                    var intent = Intent(this@LoginOtpActivity, UserActivity::class.java)
                    startActivity(intent)
                    finish()
                }

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

        if (et_phone_otp.length() == 0) {
            et_phone_otp.setError(getString(R.string.valid_error));
            return false
        }

        return true
    }
}
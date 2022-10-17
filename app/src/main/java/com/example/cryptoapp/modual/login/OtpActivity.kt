package com.example.cryptoapp.modual.login

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.provider.Telephony.Sms
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.Receiver.SmsBroadcastReceiver
import com.example.cryptoapp.Response.LoginResponse
import com.example.cryptoapp.model.LoginPayload
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.google.android.gms.auth.api.phone.SmsRetriever
import java.util.regex.Pattern

class OtpActivity : AppCompatActivity(), View.OnClickListener {
    var otp: Button? = null
    var resend_code: TextView? = null
    var otp_phone_verification: TextView? = null
    var email_otp_verification: TextView? = null
    var et_phone_otp: EditText? = null
    var et_email_otp: EditText? = null
    val REQ_USER_CONSENT = 200
    var smsBroadcastReceiver: SmsBroadcastReceiver? = null

    var email_otp: String = "123456"
    var phone_otp: String = "123456"
    var str_phone_otp: String? = null
    var str_email_otp: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        init()

    }

    fun init() {

        otp = findViewById(R.id.otp)
        resend_code = findViewById(R.id.resend_code)
        et_phone_otp = findViewById(R.id.et_phone_otp)
        et_email_otp = findViewById(R.id.et_email_otp)
        otp_phone_verification = findViewById(R.id.otp_phone_verification)
        email_otp_verification = findViewById(R.id.email_otp_verification)
        otp_phone_verification?.setText(intent.getStringExtra("phone"))
        email_otp_verification?.setText(intent.getStringExtra("email"))
        otp?.setOnClickListener(this)
        resend_code?.setOnClickListener(this)

        et_phone_otp?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var otp = et_phone_otp?.text.toString().trim()

                if (otp != phone_otp) {
                    et_phone_otp?.setError(getString(R.string.valid_otp));
                } else {
                    str_phone_otp = otp
                    Toast.makeText(
                        this@OtpActivity,
                        getString(R.string.otp_verify_done),
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        et_email_otp?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var otp = et_email_otp?.text.toString().trim()

                if (otp != email_otp) {
                    et_email_otp?.setError(getString(R.string.valid_otp));
                } else {
                    str_email_otp = otp
                    Toast.makeText(
                        this@OtpActivity,
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
        smsBroadcastReceiver!!.smsBroadcastReceiverListener =
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
            et_phone_otp!!.setText(matcher.group(0))
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
            R.id.otp -> {
                addOtp()
            }
            R.id.resend_code -> {
                et_phone_otp?.text?.clear()
                et_email_otp?.text?.clear()

                phone_otp = "123123"
                email_otp = "123123"

            }
        }
    }

    fun addOtp() {
        Log.d("test", str_phone_otp + "")
        Log.d("test", str_email_otp + "")
        var intent = Intent(this@OtpActivity, LoginActivity::class.java)
        startActivity(intent)
    }
}
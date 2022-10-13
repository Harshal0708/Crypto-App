package com.example.cryptoapp.modual.login

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.provider.Telephony.Sms
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
    var otp : Button?=null
    var otp_verification : TextView?=null
    var et_otp : EditText?=null
    val REQ_USER_CONSENT = 200
    var smsBroadcastReceiver : SmsBroadcastReceiver?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        init()

    }
    fun init(){

        otp = findViewById(R.id.otp)
        et_otp = findViewById(R.id.et_otp)
        otp_verification = findViewById(R.id.otp_verification)
        otp_verification?.setText(intent.getStringExtra("register"))
        otp?.setOnClickListener(this)

      //  startSmartUserConsent()
    }

    private fun startSmartUserConsent() {
        val client = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)

    }

    private fun registerBroadcastReceiver(){
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver!!.smsBroadcastReceiverListener = object  : SmsBroadcastReceiver.SmsBroadcastReceiverListener{
            override fun onSuccess(intent: Intent) {
                startActivityForResult(intent,REQ_USER_CONSENT)
            }

            override fun onFailure() {
                TODO("Not yet implemented")
            }

        }

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver,intentFilter)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQ_USER_CONSENT){
            if(resultCode == RESULT_OK && data != null){
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                getOtpFromMessage(message)
            }
        }
    }

    private fun getOtpFromMessage(message: String?) {
        val otpPatter = Pattern.compile("(|^)\\d{6}")
        val matcher = otpPatter.matcher(message)
        if(matcher.find()){
            et_otp!!.setText(matcher.group(0))
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

        }
    }
    fun addOtp() {

        if(intent.getBooleanExtra("isRegister",false) == true){

            var intent = Intent(this@OtpActivity, LoginActivity::class.java)
            startActivity(intent)

        }else{
            var intent = Intent(this@OtpActivity, UserActivity::class.java)
            startActivity(intent)

        }
    }


}
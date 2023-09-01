package com.example.cryptoapp.modual.login

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.example.cryptoapp.Receiver.SmsBroadcastReceiver
import com.example.cryptoapp.Response.OtpResponse
import com.example.cryptoapp.Response.RegisterResponse
import com.example.cryptoapp.Response.SendRegistrationOtpResponce
import com.example.cryptoapp.model.SendRegistrationOtpPayload
import com.example.cryptoapp.model.VerifyRegistrationOtpPayload
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.gson.Gson
import com.mukesh.mukeshotpview.completeListener.MukeshOtpCompleteListener
import com.mukesh.mukeshotpview.mukeshOtpView.MukeshOtpView
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.regex.Pattern


class OtpActivity : AppCompatActivity(), View.OnClickListener {
    //    lateinit var otp: TextView
//    lateinit var register_progressBar: ProgressBar
//
    lateinit var view: View
    lateinit var register_progressBar: ProgressBar
    lateinit var resent: TextView
    lateinit var progressBar_cardView: RelativeLayout

    lateinit var resend_code: TextView
    lateinit var txt_otp_resend: TextView
    lateinit var otp_phone_verification: TextView
    lateinit var email_otp_verification: TextView
    lateinit var txt_email_phone: TextView
    lateinit var resend_timer: TextView

    val REQ_USER_CONSENT = 200
    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver


    var email: String = ""
    var phone: String = ""
    var firsName: String = ""
    var lastName: String = ""
    var rePassword: String = ""
    lateinit var imageUri: Uri
    var countryId: String = ""
    var selectedKeyPos: Int = 0
    var selectedKeyPos1: Int = 0
    var generateOtp: String = ""
    var generateOtp1: String = ""
    lateinit var userData: MyData

    lateinit var timer: CountDownTimer


    lateinit var otp_view_1: MukeshOtpView
    lateinit var otp_view_2: MukeshOtpView
    lateinit var ima_back: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        init()

    }

    fun init() {


        resend_code = findViewById(R.id.txt_sign_in_here)
        txt_otp_resend = findViewById(R.id.txt_otp_resend)
        resend_timer = findViewById(R.id.resend_timer)

        view = findViewById(R.id.btn_progressBar)

        txt_email_phone = findViewById(R.id.txt_email_phone)


        otp_view_1 = findViewById(R.id.otp_view_1)
        otp_view_2 = findViewById(R.id.otp_view_2)
        ima_back = findViewById(R.id.ima_back)

        register_progressBar = view.findViewById(R.id.register_progressBar)
        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)

        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.verify_continue)
        progressBar_cardView.setOnClickListener(this)
        ima_back.setOnClickListener(this)

        if (intent.extras != null) {
            val jsonData = intent.getStringExtra("data")
            val gson = Gson()
            userData = gson.fromJson(jsonData, MyData::class.java)
            email = userData.email
            phone = userData.phone
            firsName = userData.firsName
            lastName = userData.lastName
            rePassword = userData.rePassword
            countryId = userData.countryId
            imageUri = userData.imageUri.toUri()
//            imageUri =bitmapToString(userData.imageUri)

        }

        //imageUri = byte

        //val byteArray = intent.getByteArrayExtra("imageUri")
        // val bmp = BitmapFactory.decodeByteArray(imageUri, 0, imageUri!!.size)

        txt_email_phone.setText("Please, enter the verification code we sent to your  Mobile ${phone} and Gmail ${email}")

        Log.d("test", email)
        Log.d("test", phone)
        Log.d("test", firsName)
        Log.d("test", lastName)
        Log.d("test", rePassword)
        Log.d("test", imageUri.toString())

        resend_code.isEnabled = false
        txt_otp_resend.isEnabled = false
        resend_code.setOnClickListener(this)
        txt_otp_resend.setOnClickListener(this)
        progressBar_cardView.setOnClickListener(this)

        otp_view_1.setOtpCompletionListener(object : MukeshOtpCompleteListener {
            override fun otpCompleteListener(otp: String?) {
                generateOtp = otp.toString()
            }
        })

        otp_view_2.setOtpCompletionListener(object : MukeshOtpCompleteListener {
            override fun otpCompleteListener(otp: String?) {
                generateOtp1 = otp.toString()
            }
        })


        countdownTimer()
        //  startSmartUserConsent()
    }

    fun bitmapToString(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun verifyRegistrationOtp(str_email: String, str_phone: String) {

        register_progressBar.visibility = View.VISIBLE
        val response = ServiceBuilder(this@OtpActivity,false).buildService(RestApi::class.java)

        //val payload = RegisterPayload(password,rePassword,email,firsName,lastName,"","","","","",phone,"Appu25")
        val payload = VerifyRegistrationOtpPayload(
            email,
            str_email,
            phone,
            str_phone
        )

        response.addVerifyRegistrationOtpp(payload)
            .enqueue(
                object : retrofit2.Callback<SendRegistrationOtpResponce> {
                    override fun onResponse(
                        call: Call<SendRegistrationOtpResponce>,
                        response: retrofit2.Response<SendRegistrationOtpResponce>
                    ) {
                        if (response.body()?.isSuccess == true) {
                            register_progressBar.visibility = View.GONE
                            addCreateAccount()
                            response.body()?.message?.let { showToast(this@OtpActivity, it) }
                        } else {
                            register_progressBar.visibility = View.GONE
                            response.body()?.message?.let { showToast(this@OtpActivity, it) }
                        }
                    }

                    override fun onFailure(call: Call<SendRegistrationOtpResponce>, t: Throwable) {
                        register_progressBar.visibility = View.GONE
                        showToast(this@OtpActivity, getString(R.string.otp_failed))
                    }

                }
            )
    }

    fun addCreateAccount() {
        register_progressBar?.visibility = View.VISIBLE
        val response = ServiceBuilder(this@OtpActivity,false).buildService(RestApi::class.java)

        val fileDir = applicationContext.filesDir
        val file = File(fileDir,"image.png")
        val inputStream = contentResolver.openInputStream(imageUri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)

        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val part = MultipartBody.Part.createFormData("ProfileImage",file.name,requestBody)
        val cusName: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), firsName)
        val cusLastName: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), lastName)
        val cusCountryId: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), countryId)
        val cusLastPassword: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), rePassword)
        val cusLastEmail: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
        val cusLastMobile: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), phone)
        val cusUri: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), file.name)

        showLog("part", part.toString())

        response.addRegister(
            cusName,
            cusLastName,
            cusLastPassword,
            cusCountryId,
            cusLastEmail,
            cusLastMobile,
            part,
            cusUri
        ).enqueue(
            object : retrofit2.Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: retrofit2.Response<RegisterResponse>
                ) {
                    if (response.body()?.isSuccess == true) {
                        register_progressBar.visibility = View.GONE
                        response.body()?.message?.let { showToast(this@OtpActivity, it) }
                        var intent = Intent(this@OtpActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        response.body()?.message?.let { showToast(this@OtpActivity, it) }
                        register_progressBar.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    register_progressBar.visibility = View.GONE
                    showToast(this@OtpActivity, getString(R.string.register_failed))
                }
            }
        )
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
            //  et_phone_otp.setText(matcher.group(0))
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

//                otp_view_1.setOtpCompletionListener {
//                    Log.d("Actual Value", it)
//                    generateOtp=it
//                }
//                otp_view_1.setOtpCompletionListener {
//                    Log.d("Actual Value", it)
//                    generateOtp1=it
//                }


                verifyRegistrationOtp(generateOtp, generateOtp1)

                //addCreateAccount()
            }
            R.id.txt_sign_in_here -> {
                resend()
            }
            R.id.txt_otp_resend -> {
                resend()
            }
            R.id.ima_back -> {
                onBackPressed()
            }


        }
    }

    fun resend() {
        resend_timer.visibility = View.VISIBLE
        addResendOtp()
        timer.start()
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


    fun addOtp(str_email: String, str_phone: String) {

        register_progressBar.visibility = View.VISIBLE
        val response = ServiceBuilder(this@OtpActivity,false).buildService(RestApi::class.java)


        response.addOtp(str_phone, str_email, email, phone)
            .enqueue(
                object : retrofit2.Callback<OtpResponse> {
                    override fun onResponse(
                        call: Call<OtpResponse>,
                        response: retrofit2.Response<OtpResponse>
                    ) {
                        if (response.body()?.code == "200") {
                            register_progressBar.visibility = View.GONE
                            response.body()?.message?.let { showToast(this@OtpActivity, it) }
//                            Log.d("test", str_phone_otp + "")
//                            Log.d("test", str_email_otp + "")
                            var intent = Intent(this@OtpActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            register_progressBar.visibility = View.GONE
                            showToast(this@OtpActivity, getString(R.string.user_not_created))
                        }

                    }

                    override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                        register_progressBar.visibility = View.GONE
                        showToast(this@OtpActivity, getString(R.string.user_not_created))
                    }

                }
            )
    }

    fun addResendOtp() {

        otp_view_1.text?.clear()
        otp_view_2.text?.clear()
        selectedKeyPos = 0
        selectedKeyPos1 = 0
        register_progressBar.visibility = View.VISIBLE
        val response = ServiceBuilder(this@OtpActivity,false).buildService(RestApi::class.java)

        val payload = SendRegistrationOtpPayload(
            email,
            firsName,
            phone,
        )

        response.addSendRegistrationOtp(payload)
            .enqueue(
                object : retrofit2.Callback<SendRegistrationOtpResponce> {
                    override fun onResponse(
                        call: retrofit2.Call<SendRegistrationOtpResponce>,
                        response: retrofit2.Response<SendRegistrationOtpResponce>
                    ) {

                        if (response.body()?.isSuccess == true) {
                            register_progressBar.visibility = View.GONE
                            response.body()?.message?.let { showToast(this@OtpActivity, it) }

                        } else {
                            register_progressBar.visibility = View.GONE
                        }

                    }

                    override fun onFailure(
                        call: Call<SendRegistrationOtpResponce>,
                        t: Throwable
                    ) {
                        register_progressBar.visibility = View.GONE
                        showToast(this@OtpActivity, getString(R.string.user_not_created))
                    }

                }
            )

    }

    fun validation(): Boolean {

//        if (et_phone_otp.length() == 0) {
//            et_phone_otp.setError(getString(R.string.valid_error));
//            return false
//        }

//        if (et_email_otp.length() == 0) {
//            et_email_otp.setError(getString(R.string.valid_error));
//            return false
//        }


        return true
    }
}
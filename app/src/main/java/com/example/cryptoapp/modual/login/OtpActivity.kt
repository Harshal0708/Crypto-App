package com.example.cryptoapp.modual.login

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
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
import retrofit2.Call
import java.util.regex.Pattern


class OtpActivity : AppCompatActivity(), View.OnClickListener {
    //    lateinit var otp: TextView
//    lateinit var register_progressBar: ProgressBar
//
    lateinit var view: View
    lateinit var otp_layout: View
    lateinit var otp_layout_two: View
    lateinit var register_progressBar: ProgressBar
    lateinit var resent: TextView
    lateinit var progressBar_cardView: RelativeLayout

    lateinit var resend_code: TextView
    lateinit var txt_otp_resend: TextView
    lateinit var otp_phone_verification: TextView
    lateinit var email_otp_verification: TextView
    lateinit var txt_email_phone: TextView
    lateinit var resend_timer: TextView

    lateinit var otp_1: EditText
    lateinit var otp_2: EditText
    lateinit var otp_3: EditText
    lateinit var otp_4: EditText
    lateinit var otp_5: EditText
    lateinit var otp_6: EditText

    lateinit var otp_two_1: EditText
    lateinit var otp_two_2: EditText
    lateinit var otp_two_3: EditText
    lateinit var otp_two_4: EditText
    lateinit var otp_two_5: EditText
    lateinit var otp_two_6: EditText

    val REQ_USER_CONSENT = 200
    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver

    lateinit var animationView: LottieAnimationView

    var email: String = ""
    var phone: String = ""
    var firsName: String = ""
    var lastName: String = ""
    var rePassword: String = ""
    lateinit var imageUri: String
    var selectedKeyPos: Int = 0
    var selectedKeyPos1: Int = 0
    lateinit var generateOtp: String
    lateinit var generateOtp1: String

    lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        init()

    }

    fun init() {

        resend_code = findViewById(R.id.txt_sign_in_here)
        txt_otp_resend = findViewById(R.id.txt_otp_resend)
        resend_timer = findViewById(R.id.resend_timer)
        animationView = findViewById(R.id.login_img)
        setupAnim()

        view = findViewById(R.id.btn_progressBar)
        otp_layout = findViewById(R.id.otp_layout)
        otp_layout_two = findViewById(R.id.otp_layout_two)
        txt_email_phone = findViewById(R.id.txt_email_phone)

        otp_1 = otp_layout.findViewById(R.id.otp_1)
        otp_2 = otp_layout.findViewById(R.id.otp_2)
        otp_3 = otp_layout.findViewById(R.id.otp_3)
        otp_4 = otp_layout.findViewById(R.id.otp_4)
        otp_5 = otp_layout.findViewById(R.id.otp_5)
        otp_6 = otp_layout.findViewById(R.id.otp_6)


        otp_two_1 = otp_layout_two.findViewById(R.id.otp_1)
        otp_two_2 = otp_layout_two.findViewById(R.id.otp_2)
        otp_two_3 = otp_layout_two.findViewById(R.id.otp_3)
        otp_two_4 = otp_layout_two.findViewById(R.id.otp_4)
        otp_two_5 = otp_layout_two.findViewById(R.id.otp_5)
        otp_two_6 = otp_layout_two.findViewById(R.id.otp_6)

        register_progressBar = view.findViewById(R.id.register_progressBar)
        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)

        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.verify_continue)
        progressBar_cardView.setOnClickListener(this)
        email = intent.getStringExtra("email").toString()
        phone = intent.getStringExtra("phone").toString()
        firsName = intent.getStringExtra("firsName").toString()
        lastName = intent.getStringExtra("lastName").toString()
        rePassword = intent.getStringExtra("rePassword").toString()
        imageUri = intent.getStringExtra("imageUri")!!
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

        countdownTimer()
        otp_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length!! > 0) {
                    showkeybord(otp_2, otp_1, false)
                } else {
                    Toast.makeText(this@OtpActivity, "Clear", Toast.LENGTH_SHORT).show()
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

                    Toast.makeText(this@OtpActivity, "Done", Toast.LENGTH_SHORT).show()
                } else {
                    showkeybord(otp_5, otp_6, true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        otp_two_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length!! > 0) {
                    showkeybord(otp_two_2, otp_two_1, false)

                } else {
                    Toast.makeText(this@OtpActivity, "Clear", Toast.LENGTH_SHORT).show()
                }
            }

        })

        otp_two_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length!! > 0) {
                    showkeybord(otp_two_3, otp_two_2, false)
                } else {
                    showkeybord(otp_two_1, otp_two_2, true)
                }
            }

        })

        otp_two_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length!! > 0) {
                    showkeybord(otp_two_4, otp_two_3, false)
                } else {
                    showkeybord(otp_two_2, otp_two_3, true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        otp_two_4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length!! > 0) {
                    showkeybord(otp_two_5, otp_two_4, false)
                } else {
                    showkeybord(otp_two_3, otp_two_4, true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        otp_two_5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length!! > 0) {
                    showkeybord(otp_two_6, otp_two_5, false)
                } else {
                    showkeybord(otp_two_4, otp_two_5, true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        otp_two_6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length!! > 0) {

                    Toast.makeText(this@OtpActivity, "Done", Toast.LENGTH_SHORT).show()
                } else {
                    showkeybord(otp_two_5, otp_two_6, true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        //  startSmartUserConsent()
    }

    fun verifyRegistrationOtp(str_email: String, str_phone: String) {

        register_progressBar?.visibility = View.VISIBLE
        val response = ServiceBuilder.buildService(RestApi::class.java)

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
                            register_progressBar?.visibility = View.GONE
                            addCreateAccount()
                            Toast.makeText(
                                this@OtpActivity,
                                response.body()?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            register_progressBar?.visibility = View.GONE
                            Toast.makeText(
                                this@OtpActivity,
                                response.body()?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }

                    override fun onFailure(call: Call<SendRegistrationOtpResponce>, t: Throwable) {
                        Log.d("test", t.toString())

                        register_progressBar?.visibility = View.GONE
                        Toast.makeText(this@OtpActivity, t.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                }
            )


    }

    fun addCreateAccount() {

        register_progressBar?.visibility = View.VISIBLE
        val response = ServiceBuilder.buildService(RestApi::class.java)

//        var payload= RegisterPayload(
//            firsName,
//            lastName,
//            rePassword,
//            email,
//            "",
//            "",
//            "",
//            "",
//            phone,
//            imageUri,
//            ""
//        )

//        var file = File(imageUri)
//        var requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
//        var filePart = MultipartBody.Part.createFormData("upload_file", file.name, requestBody)
//        showLog(filePart.toString())

        response.addRegister(
            firsName,
            lastName,
            rePassword,
            email,
            "",
            "",
            "",
            "",
            phone,
            imageUri,
            ""
        ).enqueue(
            object : retrofit2.Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: retrofit2.Response<RegisterResponse>
                ) {
                    if (response.body()?.isSuccess == true) {
                        register_progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@OtpActivity,
                            response.body()?.message,
                            Toast.LENGTH_LONG
                        ).show()
                        var intent = Intent(this@OtpActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        register_progressBar.visibility = View.GONE
//                            Toast.makeText(
//                                this@OtpActivity,
//                                response.body()?.message,
//                                Toast.LENGTH_LONG
//                            ).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    register_progressBar.visibility = View.GONE
                    Toast.makeText(this@OtpActivity, t.toString(), Toast.LENGTH_LONG)
                        .show()
                }
            }
        )
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

                generateOtp = otp_1.text.toString() +
                        otp_2.text.toString() +
                        otp_3.text.toString() +
                        otp_4.text.toString() +
                        otp_5.text.toString() +
                        otp_6.text.toString()

                generateOtp1 = otp_two_1.text.toString() +
                        otp_two_2.text.toString() +
                        otp_two_3.text.toString() +
                        otp_two_4.text.toString() +
                        otp_two_5.text.toString() +
                        otp_two_6.text.toString()


                verifyRegistrationOtp(generateOtp,generateOtp1)

                //addCreateAccount()
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
        val response = ServiceBuilder.buildService(RestApi::class.java)


        response.addOtp(str_phone, str_email, email, phone)
            .enqueue(
                object : retrofit2.Callback<OtpResponse> {
                    override fun onResponse(
                        call: Call<OtpResponse>,
                        response: retrofit2.Response<OtpResponse>
                    ) {

                        Log.d("test", response.toString())
                        Log.d("test", response.body().toString())

                        if (response.body()?.code == "200") {
                            register_progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@OtpActivity,
                                response.body()?.message,
                                Toast.LENGTH_LONG
                            ).show()

//                            Log.d("test", str_phone_otp + "")
//                            Log.d("test", str_email_otp + "")
                            var intent = Intent(this@OtpActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {

                            register_progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@OtpActivity,
                                "User not created!",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }


                    override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                        Log.d("test", t.toString())

                        register_progressBar.visibility = View.GONE
                        Toast.makeText(this@OtpActivity, t.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                }
            )


    }

    fun addResendOtp() {

        otp_1.text.clear()
        otp_2.text.clear()
        otp_3.text.clear()
        otp_4.text.clear()
        otp_5.text.clear()
        otp_6.text.clear()

        otp_two_1.text.clear()
        otp_two_2.text.clear()
        otp_two_3.text.clear()
        otp_two_4.text.clear()
        otp_two_5.text.clear()
        otp_two_6.text.clear()

        selectedKeyPos = 0
        selectedKeyPos1 = 0
        register_progressBar.visibility = View.VISIBLE
        val response = ServiceBuilder.buildService(RestApi::class.java)

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

                            Toast.makeText(
                                this@OtpActivity,
                                response.body()?.message,
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            register_progressBar.visibility = View.GONE


                            // Toast.makeText(this@RegisterActivity,response.body()?.message,Toast.LENGTH_SHORT).show()

                        }

                    }

                    override fun onFailure(
                        call: retrofit2.Call<SendRegistrationOtpResponce>,
                        t: Throwable
                    ) {


                        register_progressBar.visibility = View.GONE
                        Toast.makeText(this@OtpActivity, t.toString(), Toast.LENGTH_LONG)
                            .show()
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
package com.example.cryptoapp.modual.authenticator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.Constants
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.model.GenerateQrCodePayload
import com.example.cryptoapp.modual.login.UserActivity
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import com.mukesh.mukeshotpview.completeListener.MukeshOtpCompleteListener
import com.mukesh.mukeshotpview.mukeshOtpView.MukeshOtpView
import com.strings.cryptoapp.Response.BarcodeImageResponse
import com.strings.cryptoapp.Response.GenerateQrCodeResponnse
import com.strings.cryptoapp.model.CreateUserGAKeyPayload
import com.strings.cryptoapp.model.Verify2FAPayload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GoogleAuthenticatorActivity : AppCompatActivity() {

    lateinit var qrIV: ImageView
    lateinit var idTVKey: TextView
    lateinit var idTVKey1: TextView
    lateinit var ed_totp: MukeshOtpView

    lateinit var setupCode: String
    lateinit var userKey: String
    var isTrue: Boolean = false

    lateinit var preferences: MyPreferences
    lateinit var data: DataXX
    lateinit var view: View
    lateinit var register_progressBar: ProgressBar
    lateinit var resent: TextView
    lateinit var progressBar_cardView: RelativeLayout

    var generateOtp1: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_authenticator)

        qrIV = findViewById(R.id.idIVQrcode)
        idTVKey = findViewById(R.id.idTVKey)
        idTVKey1 = findViewById(R.id.idTVKey1)
        ed_totp = findViewById(R.id.ed_totp)

        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)

        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)
        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.login)

        data = Gson().fromJson(intent.getStringExtra("data"), DataXX::class.java)
        preferences = MyPreferences(this)

        progressBar_cardView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                if (ed_totp.length() == 0) {
                    ed_totp.setError(getString(R.string.valid_error));
                } else {
                    addVerify2FA()
                }
            }
        })

        idTVKey.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                copyTextToClipboard()
            }
        })

        idTVKey1.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                copyTextToClipboard()
            }
        })


        ed_totp.setOtpCompletionListener(object : MukeshOtpCompleteListener {
            override fun otpCompleteListener(otp: String?) {
                generateOtp1 = otp.toString()
            }
        })

        getCheckUserGAKey(data.userId)

    }

    private fun copyTextToClipboard() {
        val textToCopy = setupCode
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        idTVKey.text = textToCopy
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_LONG).show()
    }

    private fun addGenerateQrCode() {
        //register_progressBar.visibility = View.VISIBLE

        val response =
            ServiceBuilder(this@GoogleAuthenticatorActivity).buildService(RestApi::class.java)
        val payload = GenerateQrCodePayload(
            data.email
        )

        response.addGenerateQrCode(payload)
            .enqueue(object : retrofit2.Callback<BarcodeImageResponse> {
                override fun onResponse(
                    call: retrofit2.Call<BarcodeImageResponse>,
                    response: retrofit2.Response<BarcodeImageResponse>
                ) {
                    showLog("barcodeImageUrl", response.body().toString())
                    showLog("setupCode", response.body()?.data!!.setupCode)

                    userKey = response.body()?.data!!.userKey
                    setupCode = response.body()?.data!!.setupCode
                    qrIV.setImageBitmap(byteArrayToBitmap(response.body()?.data!!.barcodeImageUrl!!.toByteArray()))

                    showLog("test", response.body()?.data!!.setupCode)

                }

                override fun onFailure(call: retrofit2.Call<BarcodeImageResponse>, t: Throwable) {
                    //register_progressBar.visibility = View.GONE
                    Constants.showToast(
                        this@GoogleAuthenticatorActivity,
                        getString(R.string.login_failed)
                    )
                }
            })
    }

    private fun addCreateUserGAKey() {
        //register_progressBar.visibility = View.VISIBLE
        showLog("setupCode", setupCode)
        val response =
            ServiceBuilder(this@GoogleAuthenticatorActivity).buildService(RestApi::class.java)
        val payload = CreateUserGAKeyPayload(
            userKey,
            "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            data.userId
        )

        response.addCreateUserGAKey(payload)
            .enqueue(object : retrofit2.Callback<GenerateQrCodeResponnse> {
                override fun onResponse(
                    call: retrofit2.Call<GenerateQrCodeResponnse>,
                    response: retrofit2.Response<GenerateQrCodeResponnse>
                ) {
                    showLog("barcodeImageUrl", response.body().toString())

                    if (intent.getBooleanExtra("isChecked", false) == true) {
                        preferences.setRemember(true)
                    } else {
                        preferences.setRemember(false)
                    }

                    preferences.setLogin(data)
                    preferences.setToken(data.accessToken)

                    var intent = Intent(this@GoogleAuthenticatorActivity, UserActivity::class.java)
                    startActivity(intent)
                }

                override fun onFailure(
                    call: retrofit2.Call<GenerateQrCodeResponnse>,
                    t: Throwable
                ) {
                    //register_progressBar.visibility = View.GONE
                    Constants.showToast(
                        this@GoogleAuthenticatorActivity,
                        getString(R.string.login_failed)
                    )
                }
            })
    }

    private fun addVerify2FA() {
        //register_progressBar.visibility = View.VISIBLE

        val response =
            ServiceBuilder(this@GoogleAuthenticatorActivity).buildService(RestApi::class.java)

        val payload = Verify2FAPayload(
            generateOtp1,
            userKey,
        )


        response.addVerify2FA(payload)
            .enqueue(object : retrofit2.Callback<GenerateQrCodeResponnse> {
                override fun onResponse(
                    call: retrofit2.Call<GenerateQrCodeResponnse>,
                    response: retrofit2.Response<GenerateQrCodeResponnse>
                ) {

                    if (response.body()?.data == true) {
                        if (isTrue == false) {
                            addCreateUserGAKey()
                        } else {
                            if (intent.getBooleanExtra("isChecked", false) == true) {
                                preferences.setRemember(true)
                            } else {
                                preferences.setRemember(false)
                            }
                            preferences.setLogin(data)
                            preferences.setToken(data.accessToken)

                            var intent =
                                Intent(this@GoogleAuthenticatorActivity, UserActivity::class.java)
                            startActivity(intent)
                        }

                    } else {
                        showToast(this@GoogleAuthenticatorActivity, "Failed")
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<GenerateQrCodeResponnse>,
                    t: Throwable
                ) {
                    //register_progressBar.visibility = View.GONE
                    showToast(
                        this@GoogleAuthenticatorActivity,
                        getString(R.string.login_failed)
                    )
                }
            })
    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        val decodeResponse: ByteArray = Base64.decode(data, Base64.DEFAULT or Base64.NO_PADDING)
        val bitmap = BitmapFactory.decodeByteArray(decodeResponse, 0, decodeResponse.size)
        return bitmap
    }

    private fun getCheckUserGAKey(id: String) {
        // viewLoader.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var response =
                ServiceBuilder(this@GoogleAuthenticatorActivity).buildService(RestApi::class.java)
                    .getCheckUserGAKey(id)
            withContext(Dispatchers.Main) {
                //viewLoader.visibility = View.GONE

                if (response.body()!!.data == true) {
                    qrIV.visibility = View.GONE
                    getGAKeyByUserId(data.userId)
                } else {
                    addGenerateQrCode()
                    qrIV.visibility = View.VISIBLE
                }

            }
        }
    }

    private fun getGAKeyByUserId(id: String) {
        userKey = ""
        // viewLoader.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var response =
                ServiceBuilder(this@GoogleAuthenticatorActivity).buildService(RestApi::class.java)
                    .getGAKeyByUserId(id)
            withContext(Dispatchers.Main) {
                //viewLoader.visibility = View.GONE

                if (response.body()!!.isSuccess == true) {
                    userKey = response?.body()!!.data

                    idTVKey.visibility = View.GONE
                    isTrue = true
                }

            }
        }
    }
}
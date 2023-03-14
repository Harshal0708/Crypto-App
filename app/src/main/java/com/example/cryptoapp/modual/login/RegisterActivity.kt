package com.example.cryptoapp.modual.login

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.GetCountriesResponseItem
import com.example.cryptoapp.Response.SendRegistrationOtpResponce
import com.example.cryptoapp.model.SendRegistrationOtpPayload
import com.example.cryptoapp.modual.countries.CountriesAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.network.onItemClickListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity(), OnClickListener,onItemClickListener {


    lateinit var sp_et_email: EditText
    lateinit var mn_et_phone: EditText
    lateinit var sp_et_firstName: EditText
    lateinit var sp_et_lastName: EditText
    lateinit var sp_et_password: EditText
    lateinit var sp_et_rePassword: EditText
    lateinit var mn_et_country_code: TextView
    lateinit var cb_term_accept: CheckBox

    lateinit var view: View
    lateinit var register_progressBar: ProgressBar
    lateinit var resent: TextView
    lateinit var progressBar_cardView: RelativeLayout

    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var firsName: String
    private lateinit var lastName: String
    private lateinit var password: String
    private lateinit var rePassword: String
    private lateinit var txt_sign_in_here: TextView
    private lateinit var txt_sign_here_two: TextView
    private lateinit var reg_profile_img: ImageView

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    val PHONE_NUMBER_PATTERN = Pattern.compile(
        "[0-9]{10}"
    )

    val PASSWORD = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    )

    private val pickImage = 100
    private val pickCamera = 200
    private var imageUri: Uri? = null

    lateinit var imagePath: String

    lateinit var bs_img_camera: ImageView
    lateinit var bs_img_gallery: ImageView
    lateinit var dialog: BottomSheetDialog
    lateinit var bytesofimage: ByteArray
    lateinit var photo: Bitmap
    var encodeImageString: String = ""
    var countryId: String = ""


    lateinit var countriesAdapter: CountriesAdapter
    lateinit var rv_countryName: RecyclerView
    lateinit var viewLoader: View
    lateinit var animationView: LottieAnimationView
    lateinit var getCountriesResponseItem: ArrayList<GetCountriesResponseItem>
    lateinit var dialog1 : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        )
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), pickCamera)

        init()

    }

    fun init() {

        sp_et_firstName = findViewById(R.id.sp_et_firstName)
        sp_et_lastName = findViewById(R.id.sp_et_lastName)
        sp_et_password = findViewById(R.id.sp_et_password)
        sp_et_rePassword = findViewById(R.id.sp_et_rePassword)
        txt_sign_here_two = findViewById(R.id.txt_sign_here_two)
        reg_profile_img = findViewById(R.id.reg_profile_img)
        mn_et_country_code = findViewById(R.id.mn_et_country_code)

        sp_et_email = findViewById(R.id.sp_et_email)
        mn_et_phone = findViewById(R.id.mn_et_phone)
        cb_term_accept = findViewById(R.id.cb_term_accept)
        txt_sign_in_here = findViewById(R.id.txt_sign_in_here)

        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)

        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)
        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)

        resent.text = getString(R.string.sign_up)
        progressBar_cardView.setOnClickListener(this)
        txt_sign_in_here.setOnClickListener(this)
        txt_sign_here_two.setOnClickListener(this)
        cb_term_accept.setOnClickListener(this)
        reg_profile_img.setOnClickListener(this)
        mn_et_country_code.setOnClickListener(this)

        sp_et_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var pwd = sp_et_password.text.toString().trim()

                if (!(PASSWORD.toRegex().matches(pwd))) {
                    sp_et_password.setError(getString(R.string.valid_password))
                } else {
                    showToast(this@RegisterActivity, getString(R.string.password_verify_done))
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


        sp_et_rePassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var pwd = sp_et_rePassword.text.toString().trim()

                if (!(PASSWORD.toRegex().matches(pwd))) {
                    sp_et_rePassword.setError(getString(R.string.valid_password))
                } else {
                    showToast(this@RegisterActivity, getString(R.string.re_password_verify_done))
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun setupAnim() {
        animationView.setAnimation(R.raw.currency)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    private fun getCountries() {

        viewLoader.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var response = ServiceBuilder(this@RegisterActivity).buildService(RestApi::class.java)
                .getCountries()
            withContext(Dispatchers.Main) {
                viewLoader.visibility = GONE
                getCountriesResponseItem = response.body()!!
                rv_countryName.layoutManager = LinearLayoutManager(this@RegisterActivity)
                countriesAdapter = CountriesAdapter(
                    this@RegisterActivity,
                    getCountriesResponseItem,
                    RegisterActivity(),
                    this@RegisterActivity
                )
                rv_countryName.adapter = countriesAdapter
            }
        }
    }


    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.progressBar_cardView -> {

                email = sp_et_email.text.toString()
                phone = mn_et_phone.text.toString()
                firsName = sp_et_firstName.text.toString()
                lastName = sp_et_lastName.text.toString()
                password = sp_et_password.text.toString()
                rePassword = sp_et_rePassword.text.toString()

                if (validation() == true) {
                    sendRegistrationOtp()
                }
            }
            R.id.txt_sign_in_here -> {
                signInHere()

            }
            R.id.txt_sign_here_two -> {
                signInHere()
            }
            R.id.cb_term_accept -> {
                //Toast.makeText(this,"cb_term_accept",Toast.LENGTH_SHORT).show()
            }
            R.id.reg_profile_img -> {
                openBottomSheet()
            }
            R.id.bs_img_camera -> {
                openCamera()
                dialog.dismiss()
            }
            R.id.bs_img_gallery -> {
                openGallery()
                dialog.dismiss()
            }

            R.id.mn_et_country_code -> {
                exit()
            }

        }
    }

    fun exit() {
        dialog1 = Dialog(this, android.R.style.ThemeOverlay)
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog1.getWindow()?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        dialog1.setCancelable(true)
        dialog1.setContentView(R.layout.custom_countries)
        viewLoader = dialog1.findViewById(R.id.loader_animation)
        animationView = viewLoader.findViewById(R.id.lotti_img)
        rv_countryName = dialog1.findViewById(R.id.rv_countryName)
        setupAnim()
        getCountries()
        dialog1.show()
    }


    fun sendRegistrationOtp() {

        register_progressBar.visibility = View.VISIBLE
        val response = ServiceBuilder(this@RegisterActivity).buildService(RestApi::class.java)

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
                            register_progressBar.visibility = GONE
                            val intent = Intent(this@RegisterActivity, OtpActivity::class.java)
                            intent.putExtra("email", email)
                            intent.putExtra("phone", phone)
                            intent.putExtra("firsName", firsName)
                            intent.putExtra("lastName", lastName)
                            intent.putExtra("rePassword", rePassword)
                            intent.putExtra("imageUri", encodeImageString)
                            intent.putExtra("countryId", countryId)
                            startActivity(intent)

                            response.body()?.message?.let { showToast(this@RegisterActivity, it) }
                        } else {
                            register_progressBar.visibility = GONE
                            Toast.makeText(this@RegisterActivity, "Failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<SendRegistrationOtpResponce>,
                        t: Throwable
                    ) {
                        register_progressBar.visibility = GONE
                        showToast(this@RegisterActivity, getString(R.string.register_failed))
                    }
                }
            )
    }


    private fun signInHere() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openBottomSheet() {
        dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.profile_bottom_sheet, null)
        dialog.setCancelable(true)
        bs_img_camera = view.findViewById(R.id.bs_img_camera)
        bs_img_gallery = view.findViewById(R.id.bs_img_gallery)

        bs_img_camera.setOnClickListener(this)
        bs_img_gallery.setOnClickListener(this)

        dialog.setContentView(view)
        dialog.show()
    }

    private fun openCamera() {
        val gallery = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(gallery, pickCamera)
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == pickImage) {
            try {
                imageUri = data?.data
                val inputStream = contentResolver.openInputStream(imageUri!!)
                var bitmap = BitmapFactory.decodeStream(inputStream)
                reg_profile_img.setImageBitmap(bitmap)
                encodeBitmapImage(bitmap)
                showLog("photo", photo.toString())
            } catch (ex: Exception) {
            }
        } else if (resultCode == RESULT_OK && requestCode == pickCamera) {
            photo = data?.extras?.get("data") as Bitmap
            showLog("photo", photo.toString())
            encodeBitmapImage(photo)
            reg_profile_img.setImageBitmap(photo)
        }
    }


    private fun encodeBitmapImage(bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        bytesofimage = byteArrayOutputStream.toByteArray()
        encodeImageString = Base64.encodeToString(bytesofimage, Base64.DEFAULT)
        showLog("photo", encodeImageString.toString())
    }

    fun validation(): Boolean {

        if (sp_et_firstName.length() == 0) {
            sp_et_firstName.setError(getString(R.string.valid_error));
            return false;
        }

        if (sp_et_lastName.length() == 0) {
            sp_et_lastName.setError(getString(R.string.valid_error));
            return false;
        }

        if (sp_et_email.length() == 0) {
            sp_et_email.setError(getString(R.string.valid_error));
            return false;
        }

        email = sp_et_email.text.toString().trim()
        if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(email))) {
            sp_et_email.setError(getString(R.string.email_error));
            return false;
        }

        if (mn_et_phone.length() == 0) {
            mn_et_phone.setError(getString(R.string.valid_error));
            return false;
        }

        phone = mn_et_phone.text.toString().trim()
        if (!(PHONE_NUMBER_PATTERN.toRegex().matches(phone))) {
            mn_et_phone.setError(getString(R.string.phone_error));
            return false;
        }

        if (sp_et_password.length() == 0) {
            sp_et_password.setError(getString(R.string.password_error));
            return false;
        }

        if (sp_et_rePassword.length() == 0) {
            sp_et_rePassword.setError(getString(R.string.repassword_error));
            return false
        }

        if (!sp_et_password.text.toString().equals(sp_et_rePassword.text.toString())) {
            sp_et_rePassword.setError(getString(R.string.password_not_error))
            return false
        }

        if (cb_term_accept.isChecked == false) {
            showToast(this@RegisterActivity, getString(R.string.please_accept_condition))
            return false
        }

        return true

    }

    override fun onItemClick(pos: Int) {
        mn_et_country_code.text = "+ ${getCountriesResponseItem.get(pos).countryCode}"
        countryId = getCountriesResponseItem.get(pos).id
        dialog1.dismiss()
    }
}

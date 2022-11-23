package com.example.cryptoapp.modual.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.LoginUserDataResponse
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var edFirstname: EditText
    lateinit var edLastname: EditText
    lateinit var edEmail: EditText
    lateinit var edPhone: EditText
    lateinit var view: View
    lateinit var register_progressBar: ProgressBar
    lateinit var resent: TextView
    lateinit var progressBar_cardView: RelativeLayout

    private lateinit var email: String
    private lateinit var phone: String

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

    lateinit var preferences: MyPreferences
    lateinit var userDetail: LoginUserDataResponse

    lateinit var viewLoader: View
    lateinit var animationView: LottieAnimationView

    private val pickImage = 100
    private val pickCamera = 200
    private var imageUri: Uri? = null

    lateinit var profile_img: ImageView
    lateinit var bs_img_camera: ImageView
    lateinit var bs_img_gallery: ImageView
    lateinit var dialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        )
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), pickCamera)

        init()
    }

    private fun init() {
        preferences = MyPreferences(this)
        userDetail = Gson().fromJson(preferences.getLogin(), LoginUserDataResponse::class.java)

        edFirstname = findViewById(R.id.edFirstname)
        edLastname = findViewById(R.id.edLastname)
        edEmail = findViewById(R.id.edEmail)
        edPhone = findViewById(R.id.edPhone)
        profile_img = findViewById(R.id.reg_profile_img)
        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)

        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)
        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.save)

        viewLoader = findViewById(R.id.loader_animation)
        animationView = viewLoader.findViewById(R.id.lotti_img)

        progressBar_cardView.setOnClickListener(this)
        profile_img.setOnClickListener(this)

        getUserDetails(userDetail.userId)
        setupAnim()
    }


    private fun setupAnim() {
        animationView.setAnimation(R.raw.currency)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    private fun openCamera() {
        val gallery = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // startActivityForResult(gallery, pickCamera)
        if (gallery.resolveActivity(packageManager) != null) {
            getAction.launch(gallery)
        }


    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        if (gallery.resolveActivity(packageManager) != null) {
            getGallery.launch(gallery)
        }
    }

    val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val photo: Bitmap = it?.data?.extras?.get("data") as Bitmap
        //  imageUri = getImageUriFromBitmap(this@ProfileActivity, photo)
        profile_img.setImageBitmap(photo)
    }
    val getGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        imageUri = it.data?.data
        profile_img.setImageURI(imageUri)
    }


    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    private fun getUserDetails(id: String) {

        viewLoader.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var response = ServiceBuilder.buildService(RestApi::class.java).getUserDetails(id)
            withContext(Dispatchers.Main) {
                if (response.body()?.code.toString().equals("302")) {
                    viewLoader.visibility = View.GONE
                    edFirstname.setText(response.body()!!.data.firstName)
                    edLastname.setText(response.body()!!.data.lastName)
                    edEmail.setText(response.body()!!.data.email)
                    edPhone.setText(response.body()!!.data.phoneNumber)
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.progressBar_cardView -> {
                if (validation() == true) {
                    save()
                }
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

        }
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

    private fun save() {
        register_progressBar.visibility = View.GONE
        Toast.makeText(this@ProfileActivity, "Save", Toast.LENGTH_LONG)
            .show()
    }


    fun validation(): Boolean {

        if (edFirstname.length() == 0) {
            edFirstname.setError(getString(R.string.valid_error));
            return false;
        }

        if (edLastname.length() == 0) {
            edLastname.setError(getString(R.string.valid_error));
            return false;
        }

        if (edEmail.length() == 0) {
            edEmail.setError(getString(R.string.valid_error));
            return false;
        }

        email = edEmail.text.toString().trim()
        if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(email))) {
            edEmail.setError(getString(R.string.email_error));
            return false;
        }

        if (edPhone.length() == 0) {
            edPhone.setError(getString(R.string.valid_error));
            return false;
        }

        phone = edPhone.text.toString().trim()
        if (!(PHONE_NUMBER_PATTERN.toRegex().matches(phone))) {
            edPhone.setError(getString(R.string.phone_error));
            return false;
        }

        return true
    }

}
package com.example.cryptoapp.modual.login


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.Constants
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.Response.Userupdatedsuccessfully
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
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
    lateinit var userDetail: DataXX

    lateinit var viewLoader: View
    lateinit var animationView: LottieAnimationView

    private val pickImage = 100
    private val pickCamera = 200
    private var imageUri: Uri? = null
    var encodeImageString: String = ""

    lateinit var profile_img: ImageView
    lateinit var bs_img_camera: ImageView
    lateinit var bs_img_gallery: ImageView
    lateinit var dialog: BottomSheetDialog
    lateinit var str_array: ByteArray
    lateinit var data: DataXX

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
        userDetail = Gson().fromJson(preferences.getLogin(), DataXX::class.java)

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

        getUserDetails(userDetail.email)
        setupAnim()
    }


    private fun setupAnim() {
        animationView.setAnimation(R.raw.currency)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
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
                profile_img.setImageBitmap(bitmap)
                encodeBitmapImage(bitmap)
            } catch (ex: Exception) {
            }
        } else if (resultCode == RESULT_OK && requestCode == pickCamera) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            encodeBitmapImage(photo)
            profile_img.setImageBitmap(photo)
        }
    }

    private fun encodeBitmapImage(bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bytesofimage = byteArrayOutputStream.toByteArray()
        encodeImageString = Base64.encodeToString(bytesofimage, Base64.DEFAULT)
        //Constants.showLog(encodeImageString.toString())

    }


    private fun getUserDetails(id: String) {

        viewLoader.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var response = ServiceBuilder.buildService(RestApi::class.java).getUserDetails(id)
            withContext(Dispatchers.Main) {
                viewLoader.visibility = View.GONE

                edFirstname.setText(response.body()!!.firstName)
                edLastname.setText(response.body()!!.lastName)
                edEmail.setText(response.body()!!.email)
                edPhone.setText(response.body()!!.phoneNumber)

                if(response.body()!!.profileImage != null){
                    profile_img.setImageBitmap(byteArrayToBitmap(response.body()!!.profileImage.toByteArray()))
                }
            }
        }
    }

    fun getUpdateProfileDetail() {

        register_progressBar.visibility = View.VISIBLE
        val response = ServiceBuilder.buildService(RestApi::class.java)


        response.updateProfileDetail(
            userDetail.userId,
            edEmail.text.toString(),
            edFirstname.text.toString(),
            edLastname.text.toString(),
            encodeImageString,
            edPhone.text.toString(),
            "",
            ""
        ).enqueue(
            object : retrofit2.Callback<Userupdatedsuccessfully> {
                override fun onResponse(
                    call: Call<Userupdatedsuccessfully>,
                    response: retrofit2.Response<Userupdatedsuccessfully>
                ) {

                    register_progressBar.visibility = View.GONE

                    if(response.body()?.isSuccess == true){
                        data =response.body()?.data!!
                        preferences.setLogin(data)
                        Toast.makeText(
                            this@ProfileActivity,
                            response.body()?.message,
                            Toast.LENGTH_LONG
                        ).show()
                        var intent = Intent(this@ProfileActivity, MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(
                            this@ProfileActivity,
                            response.body()?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

                override fun onFailure(call: Call<Userupdatedsuccessfully>, t: Throwable) {
                    register_progressBar.visibility = View.GONE
//                    Toast.makeText(this@ProfileActivity, t.toString(), Toast.LENGTH_LONG)
//                        .show()
                }
            }
        )
    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        val decodeResponse: ByteArray = Base64.decode(data, Base64.DEFAULT or Base64.NO_WRAP)
        val bitmap = BitmapFactory.decodeByteArray(decodeResponse, 0, decodeResponse.size)
        return bitmap
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.progressBar_cardView -> {
                if (validation() == true) {
                    getUpdateProfileDetail()
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
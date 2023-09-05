package com.example.cryptoapp.modual.dashbord

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.Constants
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.Response.GetCountriesResponseItem
import com.example.cryptoapp.Response.Userupdatedsuccessfully
import com.example.cryptoapp.modual.countries.CountriesAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.network.onItemClickListener
import com.example.cryptoapp.preferences.MyPreferences
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.regex.Pattern


class SettingFragment : Fragment(), View.OnClickListener,
    onItemClickListener {

    lateinit var edFirstname: EditText
    lateinit var edLastname: EditText
    lateinit var edEmail: EditText
    lateinit var edPhone: EditText
    lateinit var viewDemo: View
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
    lateinit var mn_et_country_code: TextView
    lateinit var dialog1: Dialog

    lateinit var rv_countryName: RecyclerView

    lateinit var getCountriesResponseItem: ArrayList<GetCountriesResponseItem>

    lateinit var countriesAdapter: CountriesAdapter
    var countryId: String = ""
    var selectedSuperStar: String = ""
    lateinit var radioGoogle: RadioButton
    lateinit var radioOtp: RadioButton
    lateinit var radioFinger: RadioButton

    private lateinit var fragmentContext: Context
    lateinit var photo: Bitmap
    private lateinit var ima_back: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view1 = inflater.inflate(R.layout.fragment_setting, container, false)

        InIt(view1)
        return view1
    }

    private fun InIt(view1: View) {
        preferences = MyPreferences(fragmentContext)
        radioOtp = view1.findViewById(R.id.radioOtp)
        radioFinger = view1.findViewById(R.id.radioFinger)
        radioGoogle = view1.findViewById(R.id.radioGoogle)
        userDetail = Gson().fromJson(preferences.getLogin(), DataXX::class.java)

        edFirstname = view1.findViewById(R.id.edFirstname)
        edLastname = view1.findViewById(R.id.edLastname)
        edEmail = view1.findViewById(R.id.edEmail)
        edPhone = view1.findViewById(R.id.edPhone)

        mn_et_country_code = view1.findViewById(R.id.mn_et_country_code)

        profile_img = view1.findViewById(R.id.reg_profile_img)
        viewDemo = view1.findViewById(R.id.btn_progressBar)
        register_progressBar = viewDemo.findViewById(R.id.register_progressBar)

        progressBar_cardView = viewDemo.findViewById(R.id.progressBar_cardView)

        register_progressBar.visibility = View.GONE
        resent = viewDemo.findViewById(R.id.resent)
        resent.text = getString(R.string.save_profile)

        viewLoader = view1.findViewById(R.id.viewLoader)
        animationView = viewLoader.findViewById(R.id.lotti_img)

        if (MyPreferences(fragmentContext).getAuth() == 0) {
            radioGoogle.isChecked = true
            radioFinger.isChecked = false
            radioOtp.isChecked = false
        } else if (MyPreferences(fragmentContext).getAuth() == 1) {
            radioGoogle.isChecked = false
            radioFinger.isChecked = true
            radioOtp.isChecked = false
        } else if (MyPreferences(fragmentContext).getAuth() == 2) {
            radioGoogle.isChecked = false
            radioFinger.isChecked = false
            radioOtp.isChecked = true
        }


        progressBar_cardView.setOnClickListener(this)
        profile_img.setOnClickListener(this)
        mn_et_country_code.setOnClickListener(this)
        radioOtp.setOnClickListener(this)
        radioFinger.setOnClickListener(this)
        radioGoogle.setOnClickListener(this)


        edFirstname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (edFirstname.length() > 0) {
                    edFirstname.setBackground(getResources().getDrawable(R.drawable.edt_bg_selected))
                } else {
                    edFirstname.setBackground(getResources().getDrawable(R.drawable.edt_bg_normal))
                }
//
//                sp_et_firstName.setCompoundDrawablesRelativeWithIntrinsicBounds(
//                    R.drawable.ic_new_email, 0, 0, 0
//                )

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (edEmail.length() > 0) {
                    edEmail.setBackground(getResources().getDrawable(R.drawable.edt_bg_selected))
                } else {
                    edEmail.setBackground(getResources().getDrawable(R.drawable.edt_bg_normal))
                }

//
//                sp_et_firstName.setCompoundDrawablesRelativeWithIntrinsicBounds(
//                    R.drawable.ic_new_email, 0, 0, 0
//                )

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        edLastname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (edLastname.length() > 0) {
                    edLastname.setBackground(getResources().getDrawable(R.drawable.edt_bg_selected))

                } else {
                    edLastname.setBackground(getResources().getDrawable(R.drawable.edt_bg_normal))
                }
//
//                sp_et_firstName.setCompoundDrawablesRelativeWithIntrinsicBounds(
//                    R.drawable.ic_new_email, 0, 0, 0
//                )

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        edPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (edPhone.length() > 0) {
                    edPhone.setBackground(getResources().getDrawable(R.drawable.edt_bg_selected))

                } else {
                    edPhone.setBackground(getResources().getDrawable(R.drawable.edt_bg_normal))
                }
//
//                sp_et_firstName.setCompoundDrawablesRelativeWithIntrinsicBounds(
//                    R.drawable.ic_new_email, 0, 0, 0
//                )

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


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

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == pickImage) {
            try {

                imageUri = data!!.data ?: return
                profile_img.setImageURI(imageUri)

//                try {
//                    val inputStream = contentResolver.openInputStream(imageUri!!)
//                    val bitmap = BitmapFactory.decodeStream(inputStream)
//                    inputStream?.close()
//                    reg_profile_img.setImageBitmap(bitmap)
//
//                    Constants.showLog("photo", photo.toString())
//                } catch (e: IOException) {
//                    e.printStackTrace()
//
//                }

            } catch (ex: Exception) {

            }
        } else if (resultCode == AppCompatActivity.RESULT_OK && requestCode == pickCamera) {
            photo = data?.extras?.get("data") as Bitmap
            imageUri = saveImageToGallery(photo)
            profile_img.setImageURI(imageUri)

        }

    }

    private fun saveImageToGallery(bitmap: Bitmap): Uri {
        val savedImageUri = MediaStore.Images.Media.insertImage(
            fragmentContext.contentResolver,
            bitmap,
            "Captured Image",
            "Image captured from camera"
        )
        return Uri.parse(savedImageUri)
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
            var response = ServiceBuilder(fragmentContext,false).buildService(RestApi::class.java)
                .getUserDetails(id)
            withContext(Dispatchers.Main) {
                viewLoader.visibility = View.GONE

                edFirstname.setText(response.body()!!.firstName)
                edLastname.setText(response.body()!!.lastName)
                edEmail.setText(response.body()!!.email)
                edPhone.setText(response.body()!!.phoneNumber)
                mn_et_country_code.setText("+" + response.body()!!.countryCode.toString())


                if (response.body()!!.profileImage != null && response.body()!!.profileImage != "") {
//                    profile_img.setImageBitmap(byteArrayToBitmap(response.body()!!.profileImage.toByteArray()))
                    Picasso.get()
                        .load(response.body()!!.profileImage.toUri())
                        .placeholder(R.drawable.ic_app_icon)
                        .error(R.drawable.ic_app_icon)
                        .into(profile_img)

                } else {
                    profile_img.setImageDrawable(fragmentContext.getDrawable(R.drawable.ic_account))
                }
            }
        }
    }

    fun getUpdateProfileDetail() {

        register_progressBar.visibility = View.VISIBLE
        val response = ServiceBuilder(fragmentContext,false).buildService(RestApi::class.java)

//        response.updateProfileDetail(
//            userDetail.userId,
//            edEmail.text.toString(),
//            edFirstname.text.toString(),
//            edLastname.text.toString(),
//            encodeImageString,
//            edPhone.text.toString()
//        ).enqueue(
//
        val fileDir = fragmentContext.applicationContext.filesDir
        val file = File(fileDir, "image.png")
        val inputStream = fragmentContext.contentResolver.openInputStream(imageUri!!)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)

        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val part = MultipartBody.Part.createFormData("ProfileImage", file.name, requestBody)
        val cusUserId: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), userDetail.userId)
        val cusName: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), edFirstname.text.toString())
        val cusLastName: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), edLastname.text.toString())
        val cusCountryId: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), countryId)

        val cusLastEmail: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), userDetail.email)
        val cusLastMobile: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), userDetail.mobile)
        val cusUri: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), file.name)

        showLog("part", part.toString())

        response.updateProfileDetail(
            cusUserId,
            cusLastEmail,
            cusName,
            cusLastName,
            part,
            cusLastMobile
        ).enqueue(

            object : retrofit2.Callback<Userupdatedsuccessfully> {
                override fun onResponse(
                    call: Call<Userupdatedsuccessfully>,
                    response: retrofit2.Response<Userupdatedsuccessfully>
                ) {

                    register_progressBar.visibility = View.GONE

                    if (response.body()?.isSuccess == true) {
                        data = response.body()?.data!!
                        preferences.setLogin(data)
//                        Toast.makeText(
//                            this@ProfileActivity,
//                            response.body()?.message,
//                            Toast.LENGTH_LONG
//                        ).show()
                        response.body()?.message?.let { showToast(fragmentContext,requireActivity(), it) }

                        var intent = Intent(fragmentContext, MainActivity::class.java)
                        startActivity(intent)
                    } else {
//                        Toast.makeText(
//                            this@ProfileActivity,
//                            response.body()?.message,
//                            Toast.LENGTH_LONG
//                        ).show()
                        response.body()?.message?.let { showToast(fragmentContext, requireActivity(),it) }
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
            R.id.mn_et_country_code -> {
                exit()
            }
            R.id.radioGoogle -> {
                selectedSuperStar = "Google"
                showToast(fragmentContext, requireActivity(),selectedSuperStar)
                radioGoogle.isChecked = true
                radioFinger.isChecked = false
                radioOtp.isChecked = false
                MyPreferences(fragmentContext).setAuth(0)
            }

            R.id.radioFinger -> {
                selectedSuperStar = "Finger Print"
                showToast(fragmentContext,requireActivity(), selectedSuperStar)
                radioFinger.isChecked = true
                radioGoogle.isChecked = false
                radioOtp.isChecked = false
                MyPreferences(fragmentContext).setAuth(1)
            }

            R.id.radioOtp -> {
                selectedSuperStar = "Otp Print"
                showToast(fragmentContext,requireActivity(), selectedSuperStar)
                radioOtp.isChecked = true
                radioFinger.isChecked = false
                radioGoogle.isChecked = false
                MyPreferences(fragmentContext).setAuth(2)
            }

            R.id.ima_back -> {
               dialog1.dismiss()
            }
        }
    }

    fun exit() {
        dialog1 = Dialog(fragmentContext, android.R.style.ThemeOverlay)
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog1.getWindow()?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        dialog1.setCancelable(true)
        dialog1.setContentView(R.layout.custom_countries)
        viewLoader = dialog1.findViewById(R.id.viewLoader)
        ima_back = dialog1.findViewById(R.id.ima_back)
        ima_back.setOnClickListener(this)

        animationView = viewLoader.findViewById(R.id.lotti_img)
        rv_countryName = dialog1.findViewById(R.id.rv_countryName)
        setupAnim()
        getCountries()
        dialog1.show()
    }

    private fun getCountries() {

        viewLoader.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var response = ServiceBuilder(fragmentContext,false).buildService(RestApi::class.java)
                .getCountries()
            withContext(Dispatchers.Main) {
                viewLoader.visibility = View.GONE
                getCountriesResponseItem = response.body()!!
                rv_countryName.layoutManager = LinearLayoutManager(fragmentContext)
                countriesAdapter = CountriesAdapter(
                    fragmentContext,
                    getCountriesResponseItem,
                    requireActivity(),
                    this@SettingFragment
                )
                rv_countryName.adapter = countriesAdapter
            }
        }
    }

    private fun openBottomSheet() {
        dialog = BottomSheetDialog(fragmentContext)
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
            return false
        }

        if (edLastname.length() == 0) {
            edLastname.setError(getString(R.string.valid_error));
            return false
        }

        if (edEmail.length() == 0) {
            edEmail.setError(getString(R.string.valid_error));
            return false
        }

        email = edEmail.text.toString().trim()
        if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(email))) {
            edEmail.setError(getString(R.string.email_error));
            return false
        }

        if (edPhone.length() == 0) {
            edPhone.setError(getString(R.string.valid_error));
            return false
        }

        phone = edPhone.text.toString().trim()

        if (!(PHONE_NUMBER_PATTERN.toRegex().matches(phone))) {
            edPhone.setError(getString(R.string.phone_error));
            return false
        }

        return true
    }

    override fun onItemClick(pos: Int) {
        mn_et_country_code.text = "+ ${getCountriesResponseItem.get(pos).countryCode}"
        countryId = getCountriesResponseItem.get(pos).id
        dialog1.dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

}


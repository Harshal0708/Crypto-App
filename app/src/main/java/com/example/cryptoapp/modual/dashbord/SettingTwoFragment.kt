package com.example.cryptoapp.modual.dashbord

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.modual.login.LoginActivity
import com.example.cryptoapp.modual.login.ProfileActivity
import com.example.cryptoapp.modual.login.ResetPasswordActivity
import com.example.cryptoapp.modual.subscription.SubscriptionActivity
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import com.squareup.picasso.Picasso


class SettingTwoFragment : Fragment(), View.OnClickListener {

    lateinit var txt_setting_subscription: TextView
    lateinit var txt_setting_password: TextView
    lateinit var txt_setting_share_app: TextView
    lateinit var txt_setting_privacy_policy: TextView
    lateinit var txt_setting_logout: TextView
    lateinit var txt_setting_username: TextView
    lateinit var img_setting_profile: ImageView

    lateinit var con_auto_coin: ConstraintLayout
    lateinit var con_finger: ConstraintLayout
    lateinit var con_otp: ConstraintLayout

    lateinit var preferences: MyPreferences

    lateinit var data : DataXX

    private lateinit var fragmentContext: Context

    var selectedSuperStar: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_setting_two, container, false)
        InIt(view)
        return view
    }
    private fun InIt(view: View) {
        preferences = MyPreferences(requireContext())
        txt_setting_subscription = view.findViewById(R.id.txt_setting_subscription)
        txt_setting_password = view.findViewById(R.id.txt_setting_password)
        txt_setting_share_app = view.findViewById(R.id.txt_setting_share_app)
        txt_setting_privacy_policy = view.findViewById(R.id.txt_setting_privacy_policy)
        txt_setting_logout = view.findViewById(R.id.txt_setting_logout)
        txt_setting_username = view.findViewById(R.id.txt_setting_username)
        img_setting_profile = view.findViewById(R.id.img_setting_profile)

        con_auto_coin = view.findViewById(R.id.con_auto_coin)
        con_finger = view.findViewById(R.id.con_finger)
        con_otp = view.findViewById(R.id.con_otp)

        data= Gson().fromJson(preferences.getLogin(), DataXX::class.java)
        txt_setting_username.text= data.name.toString()

        if (data.imageURL != null && data.imageURL != "") {
//            img_setting_profile.setImageBitmap(byteArrayToBitmap(data.imageURL.toByteArray()))
//            profile_img.setImageBitmap(byteArrayToBitmap(response.body()!!.profileImage.toByteArray()))
            Picasso.get()
                .load(data.imageURL)
                .placeholder(R.drawable.ic_app_icon)
                .error(R.drawable.ic_app_icon)
                .into(img_setting_profile)
        }


        img_setting_profile.setOnClickListener(this)
        txt_setting_subscription.setOnClickListener(this)
        txt_setting_password.setOnClickListener(this)
        txt_setting_share_app.setOnClickListener(this)
        txt_setting_privacy_policy.setOnClickListener(this)
        txt_setting_logout.setOnClickListener(this)

        con_auto_coin.setOnClickListener(this)
        con_finger.setOnClickListener(this)
        con_otp.setOnClickListener(this)
    }
    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        val decodeResponse: ByteArray = Base64.decode(data, Base64.DEFAULT or Base64.NO_WRAP)
        val bitmap = BitmapFactory.decodeByteArray(decodeResponse, 0, decodeResponse.size)
        return bitmap
    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.img_setting_profile -> {
//                val intent = Intent(activity, ProfileActivity::class.java)
//                startActivity(intent)
                loadFragment(SettingFragment())
            }
            R.id.txt_setting_subscription -> {
                val intent = Intent(activity, SubscriptionActivity::class.java)
                startActivity(intent)
            }
            R.id.txt_setting_password -> {
                val intent = Intent(activity, ResetPasswordActivity::class.java)
                startActivity(intent)
                activity?.finish()

            }
            R.id.txt_setting_share_app -> {
                Constants.showToast(requireContext(), requireActivity(),"Share App")
//                val intent = Intent(activity, PaymentActivity::class.java)
//                startActivity(intent)
            }
            R.id.txt_setting_privacy_policy -> {
                Constants.showToast(requireContext(), requireActivity(),"Privacy Policy")
            }
            R.id.txt_setting_logout -> {
                preferences.setRemember(false)
                preferences.setToken("")
                preferences.setLogin(null)
                var intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
                Constants.showToast(requireContext(),requireActivity(), getString(R.string.logout_successfully))
            }
            R.id.con_auto_coin -> {

                selectedSuperStar = "Google"
                Constants.showToast(fragmentContext, requireActivity(), selectedSuperStar)
                MyPreferences(fragmentContext).setAuth(0)

                con_auto_coin.setBackground(
                    ContextCompat.getDrawable(
                       requireContext(),
                        R.drawable.coin_select_background
                    )
                )
                con_finger.setBackground(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.coin_background
                    )
                )
                con_otp.setBackground(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.coin_background
                    )
                )

            }
            R.id.con_finger -> {

                selectedSuperStar = "Finger Print"
                Constants.showToast(fragmentContext, requireActivity(), selectedSuperStar)
                MyPreferences(fragmentContext).setAuth(1)

                con_auto_coin.setBackground(
                    ContextCompat.getDrawable(
                       requireContext(),
                        R.drawable.coin_background
                    )
                )
                con_finger.setBackground(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.coin_select_background
                    )
                )
                con_otp.setBackground(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.coin_background
                    )
                )

            }

             R.id.con_otp -> {

                 selectedSuperStar = "Otp Print"
                 Constants.showToast(fragmentContext, requireActivity(), selectedSuperStar)
                 MyPreferences(fragmentContext).setAuth(2)

                con_auto_coin.setBackground(
                    ContextCompat.getDrawable(
                       requireContext(),
                        R.drawable.coin_background
                    )
                )
                con_finger.setBackground(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.coin_background
                    )
                )
                con_otp.setBackground(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.coin_select_background
                    )
                )

            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack("SettingTwoFragment")
        transaction.commit()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }
}
package com.example.cryptoapp.modual.login.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.R
import com.example.cryptoapp.modual.login.adapter.DocSpinnerAdapter
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DocumentFragment : Fragment(), View.OnClickListener {

    var aadhar_card_number: EditText? = null
    var pan_number: EditText? = null
    var doc_submit: Button? = null
    lateinit var spinner: Spinner
    lateinit var viewLoader: View
    lateinit var animationView: LottieAnimationView
    lateinit var adapter:DocSpinnerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_document, container, false)
        init(view)
        return view
    }

    fun init(view: View) {
        aadhar_card_number = view.findViewById(R.id.aadhar_card_number)
        pan_number = view.findViewById(R.id.pan_number)
        doc_submit = view.findViewById(R.id.doc_submit)
        doc_submit?.setOnClickListener(this)

        viewLoader = view.findViewById(R.id.loader_animation)
        animationView = viewLoader.findViewById(R.id.lotti_img)
        pan_number?.visibility=View.GONE
        aadhar_card_number?.visibility=View.GONE
        spinner = view.findViewById(R.id.doc_spinner) as Spinner

        setupAnim()
        getDocumentsByCountry()

        aadhar_card_number?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var aadhar_card = aadhar_card_number?.text.toString().trim()

                if (aadhar_card.length != 12) {
                    aadhar_card_number?.setError(getString(R.string.valid_aadhar_number));
                } else {
                    Toast.makeText(activity, "Aadhar card verify done", Toast.LENGTH_SHORT).show()

                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        pan_number?.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var pan_card = pan_number?.text.toString().trim()

                if (pan_card.length != 10) {
                    pan_number?.setError(getString(R.string.valid_pan_number))
                } else {
                    Toast.makeText(activity, "PAN card verify done", Toast.LENGTH_SHORT).show()
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

    private fun getDocuments() {
        viewLoader.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var response = ServiceBuilder(requireContext()).buildService(RestApi::class.java)
                .getDocuments()
            withContext(Dispatchers.Main) {
                viewLoader.visibility = View.GONE
                showLog("getDocuments", response.body().toString())
            }
        }
    }

    private fun getDocumentsByCountry() {
        viewLoader.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var response = ServiceBuilder(requireContext()).buildService(RestApi::class.java)
               // .getDocumentsByCountry("3319df1e-bdfd-4525-8baf-08db08fd45f0")
                .getDocumentsByCountry("179fc3dc-42b6-4985-74bd-08db13d960a2")
            withContext(Dispatchers.Main) {
                viewLoader.visibility = View.GONE

                if (spinner != null) {
                    adapter = DocSpinnerAdapter(context, response.body())
                    spinner.adapter = adapter

                    spinner.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>,
                                                    view: View, position: Int, id: Long) {

                            showLog("documentName",response.body()!!.get(position).documentName)

                            if(response.body()!!.get(position).documentName.equals("Pancard")){
                                pan_number?.visibility=View.VISIBLE
                            }else if(response.body()!!.get(position).documentName.equals("aadhar card")){
                                aadhar_card_number?.visibility=View.VISIBLE
                            }

                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // write code to perform some action
                        }

                    }
                }
                //getDocuments()
                showLog("getDocumentsByCountry", response.body().toString())
            }
        }
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.doc_submit -> {
                btVerify()
            }
        }
    }

    fun btVerify(): Boolean {
        if (aadhar_card_number?.length() == 0) {

            aadhar_card_number?.setError(getString(R.string.valid_error));
            return false;
        }

        if (pan_number?.length() == 0) {

            pan_number?.setError(getString(R.string.valid_error));
            return false;
        }

        return true
    }
}
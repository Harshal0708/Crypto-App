package com.example.cryptoapp.modual.login.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.cryptoapp.R
import java.util.regex.Pattern


class DocumentFragment : Fragment(), View.OnClickListener {

    var aadhar_card_number: EditText? = null
    var pan_number: EditText? = null
    var doc_submit: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_document, container, false)
        init(view)
        return view
    }

    fun init(view: View){
        aadhar_card_number = view.findViewById(R.id.aadhar_card_number)
        pan_number = view.findViewById(R.id.pan_number)
        doc_submit = view.findViewById(R.id.doc_submit)
        doc_submit?.setOnClickListener(this)
        aadhar_card_number?.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var aadhar_card = aadhar_card_number?.text.toString().trim()

                    if (aadhar_card.length != 12) {
                        aadhar_card_number?.setError(getString(R.string.valid_aadhar_number));
                    }else{
                        Toast.makeText(activity,"Aadhar card verify done",Toast.LENGTH_SHORT).show()

                    }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        pan_number?.addTextChangedListener(object :TextWatcher{
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

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.doc_submit -> {
                btVerify()
            }

        }
    }

    fun btVerify() :Boolean{
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
package com.nodesagency.formvalidator.example


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nodesagency.formvalidator.validators.TextInputValidator
import kotlinx.android.synthetic.main.fragment_custom_form.*
import java.lang.NumberFormatException

/**
 * A simple [Fragment] subclass.
 */
class CustomFormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_form, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupForm()
    }


    private fun setupForm() {

        customForm.setFormValidListener {
            submitBtn.isEnabled = it
        }

        // Custom Validators
        validatableEt1.validator = object : TextInputValidator() {
            override fun validate(value: String): Boolean {
                return value.length == 5
            }
        }

        validatableEt2.validator = TextInputValidator {
            try {
                it.toInt()
                it.length == 4
            } catch (nfe: NumberFormatException) {
                false
            }
        }


    }

}

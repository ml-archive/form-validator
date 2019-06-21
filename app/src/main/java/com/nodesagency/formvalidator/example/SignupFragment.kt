package com.nodesagency.formvalidator.example


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nodesagency.formvalidator.base.FormErrorMessageHandler
import kotlinx.android.synthetic.main.fragment_signup.*


class SignupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SavedState", "Restore State: $savedInstanceState")
        if (savedInstanceState != null) {
            signupForm.restoreFromBundle(savedInstanceState)
        }
        setupForm()
    }

    private fun setupForm() {


        // Listen to form validity
        signupForm.setFormValidListener {
            signupBtn.isEnabled = it
        }

        signupBtn.setOnClickListener {
            if (signupForm.validateAll()) {
                showToast("Create: ${signupForm.retrieveAll().toSignupRequest()}")
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val bundle = signupForm.retrieveAsBundle()
        outState.putAll(bundle)
        Log.d("SavedState", "Saved State: $bundle")
    }


    data class SignupRequest(val email: String, val password: String, val terms: Boolean)

    private fun Map<Int, Any?>.toSignupRequest() : SignupRequest {
        return SignupRequest(
            email = get(R.id.emailEt) as String,
            password = get(R.id.passwordEt) as String,
            terms = get(R.id.checkbox) as Boolean
        )
    }

}

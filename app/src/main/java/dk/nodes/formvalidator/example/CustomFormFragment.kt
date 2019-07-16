package dk.nodes.formvalidator.example


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nodes.formvalidator.FormLayout
import com.nodes.formvalidator.base.BaseValidator
import com.nodes.formvalidator.base.FormErrorMessageHandler
import com.nodes.formvalidator.base.FormErrorMessageResolver
import com.nodes.formvalidator.validators.TextInputValidator
import kotlinx.android.synthetic.main.fragment_custom_form.*

/**
 * A simple [Fragment] subclass.
 */
class CustomFormFragment : Fragment(), FormErrorMessageHandler, FormErrorMessageResolver{

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.nodes.formvalidator.example.R.layout.fragment_custom_form, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupForm()
        setupListeners()
    }

    private fun setupListeners() {

        radioGroups.setOnCheckedChangeListener { radioGroup, i ->
            when(i) {
                com.nodes.formvalidator.example.R.id.modeFocus -> customForm.errorHandlerMode = FormLayout.ErrorHandlerMode.Focus
                com.nodes.formvalidator.example.R.id.modeIme -> customForm.errorHandlerMode = FormLayout.ErrorHandlerMode.Ime
                com.nodes.formvalidator.example.R.id.modeManual -> customForm.errorHandlerMode = FormLayout.ErrorHandlerMode.Manual
            }
        }

        submitBtn.setOnClickListener {
            if (customForm.validateAll()) {
                showToast("Looks fine!")
            } else {
                showToast("Somethings wrong")
            }
        }

        customFormClearBtn.setOnClickListener {
            customForm.clear()
        }
    }

    private fun setupForm() {

        customForm.setFormValidListener {
            submitBtn.isEnabled = it
        }

        // Custom Validators
        validatableEt1.validator = TextInputValidator { it.length == 5 }


        // Custom Error Messages
        validatableEt1.errorMessage = "Totally wrong"
        validatableEt2.requiredMessage = "Required, doctor's orders"

        validatableEt2.validator = dk.nodes.formvalidator.example.CustomValidator()

        customForm.setErrorMessagesHandler(this)
        customForm.setErrorMessageResolver(this)

        validatableEt3.setErrorHandler {
            // Act when field error received for this field
            AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage(it)
                .show()
        }

    }

    override fun onFieldError(view: View, message: String) {
        when(view.id) {
            com.nodes.formvalidator.example.R.id.validatableEt1 -> showToast(message)
            com.nodes.formvalidator.example.R.id.validatableEt2 -> showSnackbar(message)
            else -> AlertDialog.Builder(context).setTitle("Error").setMessage(message).show()
        }
    }

    override fun resolveValidatorErrorMessage(validator: BaseValidator<*>): String {
        return "This is custom error"
    }
}

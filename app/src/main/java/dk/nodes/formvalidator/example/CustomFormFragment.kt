package dk.nodes.formvalidator.example


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.nodes.formvalidator.FormLayout
import dk.nodes.formvalidator.base.BaseValidator
import dk.nodes.formvalidator.base.FormErrorMessageHandler
import dk.nodes.formvalidator.base.FormErrorMessageResolver
import dk.nodes.formvalidator.validators.TextInputValidator
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
        return inflater.inflate(R.layout.fragment_custom_form, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupForm()
        setupListeners()
    }

    private fun setupListeners() {

        radioGroups.setOnCheckedChangeListener { radioGroup, i ->
            when(i) {
                R.id.modeFocus -> customForm.errorHandlerMode = FormLayout.ErrorHandlerMode.Focus
                R.id.modeIme -> customForm.errorHandlerMode = FormLayout.ErrorHandlerMode.Ime
                R.id.modeManual -> customForm.errorHandlerMode = FormLayout.ErrorHandlerMode.Manual
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

        validatableEt2.validator = CustomValidator()

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
            R.id.validatableEt1 -> showToast(message)
            R.id.validatableEt2 -> showSnackbar(message)
            else -> AlertDialog.Builder(context).setTitle("Error").setMessage(message).show()
        }
    }

    override fun resolveValidatorErrorMessage(validator: BaseValidator<*>): String {
        return "This is custom error"
    }
}

package dk.nodes.formvalidator.utils

import android.content.Context
import dk.nodes.formvalidator.R
import dk.nodes.formvalidator.base.BaseValidator
import dk.nodes.formvalidator.base.FormErrorMessageResolver
import dk.nodes.formvalidator.validators.*
import dk.nodes.formvalidator.validators.password.PasswordStreinght
import dk.nodes.formvalidator.validators.password.PasswordValidator

/**
 * Default implementation of error messages resolver
 * @see FormErrorMessageResolver
 */
open class DefaultErrorMessagesResolver(private val context: Context) : FormErrorMessageResolver {

    override fun resolveValidatorErrorMessage(validator: BaseValidator<*>): String {

        if (validator is MinLengthValidator)
            return context.getString(R.string.error_min_length, validator.min)

        val resource = when (validator) {
            is EmailValidator -> R.string.error_invalid_email
            is NumberValidator -> R.string.errror_invalid_number
            is PasswordValidator -> handlePasswordError(validator.streinght)
            is IdenticalValidator -> R.string.error_mismatch
            is RequiredValidator -> R.string.error_required
            is NameValidator -> R.string.error_invalid_name
            is CheckboxValidator -> R.string.error_checkbox
            else -> R.string.error_invalid

        }
        return context.getString(resource)
    }

    private fun handlePasswordError(passwordStreinght: PasswordStreinght): Int {
        return when (passwordStreinght) {
            PasswordStreinght.Weak -> R.string.error_invalid_password_weak
            PasswordStreinght.Medium -> R.string.error_invalid_password_medium
            PasswordStreinght.Strong -> R.string.error_invalid_password_strong
            PasswordStreinght.None -> 0
        }
    }
}
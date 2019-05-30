package com.nodesagency.formvalidator

import android.content.Context
import com.nodesagency.formvalidator.base.FormErrorMessageResolver
import com.nodesagency.formvalidator.validators.*
import com.nodesagency.formvalidator.validators.password.PasswordStreinght
import com.nodesagency.formvalidator.validators.password.PasswordValidator

/**
 * Default implementation of error messages resolver
 * @see FormErrorMessageResolver
 */
open class DefaultErrorMessagesResolver(private val context: Context) : FormErrorMessageResolver {

    override fun resolveValidatorErrorMessage(textInputValidator: TextInputValidator): String {
        val resource = when (textInputValidator) {
            is EmailValidator -> R.string.error_invalid_email
            is NumberValidator -> R.string.errror_invalid_number
            is PasswordValidator -> handlePasswordError(textInputValidator.streinght)
            is IdenticalValidator -> R.string.error_mismatch
            is RequiredValidator -> R.string.error_required
            is NameValidator -> R.string.error_invalid_name
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
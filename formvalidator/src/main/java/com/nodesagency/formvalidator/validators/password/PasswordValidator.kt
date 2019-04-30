package com.nodesagency.formvalidator.validators.password

import com.nodesagency.formvalidator.utils.Password
import com.nodesagency.formvalidator.validators.TextInputValidator


class PasswordValidator(val streinght: PasswordStreinght) : TextInputValidator() {
    override fun validate(value: String): Boolean {
       return when(streinght) {
           PasswordStreinght.Weak ->  value.length >= 6
           PasswordStreinght.Medium ->  value.matches(Regex(Password.Medium))
           PasswordStreinght.Strong ->  value.matches(Regex(Password.Strong))
       }
    }
}
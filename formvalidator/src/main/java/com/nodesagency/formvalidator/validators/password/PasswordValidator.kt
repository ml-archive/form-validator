package com.nodesagency.formvalidator.validators.password

import com.nodesagency.formvalidator.utils.RegexPatterns
import com.nodesagency.formvalidator.validators.TextInputValidator


class PasswordValidator(private val streinght: PasswordStreinght) : TextInputValidator() {
    override fun validate(value: String): Boolean {
       return when(streinght) {
           PasswordStreinght.Weak ->  value.length >= 6
           PasswordStreinght.Medium ->  value.matches(Regex(RegexPatterns.Password.Medium))
           PasswordStreinght.Strong ->  value.matches(Regex(RegexPatterns.Password.Strong))
       }
    }
}
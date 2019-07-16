package dk.nodes.formvalidator.validators.password

import dk.nodes.formvalidator.utils.RegexPatterns
import dk.nodes.formvalidator.validators.TextInputValidator


class PasswordValidator(val streinght: PasswordStreinght) : TextInputValidator() {
    override fun validate(value: String): Boolean {
       return when(streinght) {
           PasswordStreinght.Weak ->  value.length >= 6
           PasswordStreinght.Medium ->  value.matches(Regex(RegexPatterns.Password.Medium))
           PasswordStreinght.Strong ->  value.matches(Regex(RegexPatterns.Password.Strong))
           PasswordStreinght.None -> true
       }
    }
}
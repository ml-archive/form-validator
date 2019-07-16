package dk.nodes.formvalidator.example

import com.nodes.formvalidator.validators.TextInputValidator
import java.lang.NumberFormatException

class CustomValidator : TextInputValidator() {
    override fun validate(value: String): Boolean {
        return try {
            value.length == 4
        } catch (nfe: NumberFormatException) {
            false
        }
    }
}
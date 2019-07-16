package dk.nodes.formvalidator.validators

import com.nodes.formvalidator.ValidatableEditText

class IdenticalValidator(private val validatableEditText: ValidatableEditText) : TextInputValidator() {
    override fun validate(value: String): Boolean {
        return value == validatableEditText.text?.toString() ?: ""
    }
}
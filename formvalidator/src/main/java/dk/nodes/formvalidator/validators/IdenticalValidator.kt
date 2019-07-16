package dk.nodes.formvalidator.validators

import dk.nodes.formvalidator.ValidatableEditText

class IdenticalValidator(private val validatableEditText: ValidatableEditText) : TextInputValidator() {
    override fun validate(value: String): Boolean {
        return value == validatableEditText.text?.toString() ?: ""
    }
}
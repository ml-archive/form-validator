package com.nodesagency.formvalidator.validators

import com.nodesagency.formvalidator.ValidatableEditText

class IdenticalValidator(private val validatableEditText: ValidatableEditText) : TextInputValidator() {
    override fun validate(value: String): Boolean {
        return value == validatableEditText.text?.toString() ?: ""
    }
}
package com.nodesagency.formvalidator.validators

class OptionalValidator : TextInputValidator() {
    override fun validate(value: String): Boolean {
        return true
    }
}
package com.nodesagency.formvalidator.validators

import android.util.Patterns

class EmailValidator : TextInputValidator() {
    override fun validate(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}
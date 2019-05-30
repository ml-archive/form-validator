package com.nodesagency.formvalidator.example

import com.nodesagency.formvalidator.validators.TextInputValidator
import org.w3c.dom.Text
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
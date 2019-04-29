package com.nodesagency.formvalidator.validators

import com.nodesagency.formvalidator.base.BaseValidator

open class TextInputValidator : BaseValidator<String> {
    override fun validate(value: String): Boolean {
        return value.isNotBlank()
    }
}
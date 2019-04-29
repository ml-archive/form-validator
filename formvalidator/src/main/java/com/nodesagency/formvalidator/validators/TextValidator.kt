package com.nodesagency.formvalidator.validators

import com.nodesagency.formvalidator.base.BaseValidator

class TextValidator : BaseValidator<String> {
    override fun validate(value: String): Boolean {
        return value.isNotEmpty()
    }
}
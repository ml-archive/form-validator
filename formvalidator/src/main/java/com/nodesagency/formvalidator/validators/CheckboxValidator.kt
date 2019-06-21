package com.nodesagency.formvalidator.validators

import com.nodesagency.formvalidator.base.BaseValidator

class CheckboxValidator(private val isRequired: Boolean) : BaseValidator<Boolean> {

    override fun validate(value: Boolean): Boolean {
        return if (isRequired) value else true
    }
}
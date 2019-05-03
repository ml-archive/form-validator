package com.nodesagency.formvalidator.validators

import com.nodesagency.formvalidator.utils.RegexPatterns

class NameValidator : TextInputValidator() {

    override fun validate(value: String): Boolean {
        return value.matches(Regex(RegexPatterns.Name))
    }
}
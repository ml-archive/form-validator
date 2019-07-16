package dk.nodes.formvalidator.validators

import dk.nodes.formvalidator.utils.RegexPatterns

class NameValidator : TextInputValidator() {

    override fun validate(value: String): Boolean {
        return value.matches(Regex(RegexPatterns.Name))
    }
}
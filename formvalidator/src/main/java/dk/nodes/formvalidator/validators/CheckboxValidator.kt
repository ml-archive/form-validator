package dk.nodes.formvalidator.validators

import com.nodes.formvalidator.base.BaseValidator

class CheckboxValidator(private val isRequired: Boolean) : BaseValidator<Boolean> {

    override fun validate(value: Boolean): Boolean {
        return if (isRequired) value else true
    }
}
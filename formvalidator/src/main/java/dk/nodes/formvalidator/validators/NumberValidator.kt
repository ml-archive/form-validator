package dk.nodes.formvalidator.validators

class NumberValidator : TextInputValidator() {
    override fun validate(value: String): Boolean {
        return value.toDoubleOrNull() !=  null
    }
}
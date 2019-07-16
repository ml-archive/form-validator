package dk.nodes.formvalidator.validators

class RequiredValidator(private val isRequired: Boolean): TextInputValidator(){
    override fun validate(value: String): Boolean {
        return if (isRequired) value.isNotBlank() else true
    }
}
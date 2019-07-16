package dk.nodes.formvalidator.validators

fun  TextInputValidator(validatorBlock: (String) -> Boolean) = object : TextInputValidator() {
    override fun validate(value: String): Boolean {
        return validatorBlock.invoke(value)
    }
}
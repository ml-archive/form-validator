package dk.nodes.formvalidator.validators


class MinLengthValidator(val min: Int? = null) : TextInputValidator() {

    init {
        if (min != null && min < 0) throw IllegalArgumentException("Can't assign negative minimal value")
    }

    override fun validate(value: String): Boolean {
        return if (min == null) true else value.length >= min
    }
}
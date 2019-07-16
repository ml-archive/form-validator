package dk.nodes.formvalidator.base

/**
 * Interface to validate generic value
 */
interface BaseValidator<in T>  {
    fun validate(value: T) : Boolean
}
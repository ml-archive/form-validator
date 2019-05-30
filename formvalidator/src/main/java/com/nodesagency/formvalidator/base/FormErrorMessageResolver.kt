package com.nodesagency.formvalidator.base

import com.nodesagency.formvalidator.validators.TextInputValidator

/**
 * An interface that is used to resolve default error messages for the text validators
 * When specific error message is not specified, default implementation of this interface is used to resolve it
 */
interface FormErrorMessageResolver {

    /**
     * @param validator - validator thatt caught an error
     * @return the error message specific to validator specified
     */
    fun resolveValidatorErrorMessage(validator: BaseValidator<*>) : String

}
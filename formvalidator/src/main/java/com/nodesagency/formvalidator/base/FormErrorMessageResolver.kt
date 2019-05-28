package com.nodesagency.formvalidator.base

import com.nodesagency.formvalidator.validators.TextInputValidator

interface FormErrorMessageResolver {

    /**
     * @param validator - validator thatt caught an error
     * @return the error message specific to validator specified
     */
    fun resolveValidatorErrorMessage(validator: TextInputValidator) : String

}
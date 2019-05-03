package com.nodesagency.formvalidator.base

import com.nodesagency.formvalidator.validators.TextInputValidator

interface ErrorMessageHandler {

    /**
     * @param textInputValidator - validator thant caught an error
     * @return the error message specific to validator specified
     */
    fun handleTextValidatorError(textInputValidator: TextInputValidator) : String

}
package com.nodesagency.formvalidator.base

import com.nodesagency.formvalidator.validators.OptionalValidator
import com.nodesagency.formvalidator.validators.TextInputValidator

interface Validatable  {

    /**
     * Validates the the field
     * @return true if field is valid, false otherwise
     * @param showError indicates whether field should display the error in case of the bad input
     */
    fun validate(showError: Boolean = false) : Boolean

    /**
     * Adds a listener for this field
     * @param listenerValidatable listener to add
     */
    fun addFieldValidListener(listenerValidatable: ValidatableFieldListener)
}
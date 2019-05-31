package com.nodesagency.formvalidator.base

/**
 * interface to provide callbacks for Validatable fields
 */
interface ValidatableFieldListener {

    /**
     * Called when field gets a new isValid value
     * @param validatable - caller
     * @param isValid - new validity value
     */
    fun onFieldValidityChanged(validatable: Validatable<*>, isValid: Boolean)

    /**
     * Called when user confirms his input
     * @param validatable - caller
     */
    fun onInputConfirmed(validatable: Validatable<*>)

}
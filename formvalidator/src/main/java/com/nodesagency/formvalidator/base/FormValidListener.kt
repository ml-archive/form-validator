package com.nodesagency.formvalidator.base

/**
 * Interface with FormLayout callbacks
 */
interface FormValidListener {

    /**
     * Called when form's status changes
     * @param isValid indicates new form state
     */
    fun onFormValidityChanged(isValid: Boolean)
}
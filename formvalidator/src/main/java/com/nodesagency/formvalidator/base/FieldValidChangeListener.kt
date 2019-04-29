package com.nodesagency.formvalidator.base

interface FieldValidChangeListener {

    fun onFieldValidityChanged(validatable: Validatable, isValid: Boolean)
}
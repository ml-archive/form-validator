package com.nodesagency.formvalidator.base

interface FormValidListener {
    fun onFormValidityChanged(isValid: Boolean)
}
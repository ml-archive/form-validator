package com.nodesagency.formvalidator.base

import android.view.View

interface FormErrorMessageHandler {
    /**
     * @param view - validatable view
     * @param message Error message itself
     */
    fun onFieldError(view: View, message: String)
}
package com.nodesagency.formvalidator.base

import android.view.View

interface ErrorMessageListener {
    /**
     * @param view - validatable view
     * @param message Error message itself
     */
    fun onError(view: View, message: String)
}
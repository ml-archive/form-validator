package dk.nodes.formvalidator.base

import android.view.View

/**
 * An interface that is used to provide a custom action for a validatable view when error occurs
 * It can be provided either to the validatable view specifically of for all the views in the FormLayout
 *
 */
interface FormErrorMessageHandler {
    /**
     * @param view - validatable view
     * @param message Error message itself
     */
    fun onFieldError(view: View, message: String)
}
package com.nodesagency.formvalidator

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox
import com.nodesagency.formvalidator.base.FormErrorMessageHandler
import com.nodesagency.formvalidator.base.FormErrorMessageResolver
import com.nodesagency.formvalidator.base.Validatable
import com.nodesagency.formvalidator.base.ValidatableFieldListener
import com.nodesagency.formvalidator.utils.Logger
import com.nodesagency.formvalidator.validators.CheckboxValidator

class ValidatableCheckbox : CheckBox, Validatable {

    constructor(context: Context) : super(context, null) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    override var isRequired: Boolean = false
        set(value) {
            field = value
            validator = CheckboxValidator(value)
        }

    var errorMessage: String? = null


    override var formErrorMessageResolver: FormErrorMessageResolver = DefaultErrorMessagesResolver(context)

    override var formErrorMessageHandler: FormErrorMessageHandler? = null

    private val checkboxListeners: MutableList<ValidatableFieldListener> = mutableListOf()

    private var validator: CheckboxValidator = CheckboxValidator(isRequired)

    private fun init(attrs: AttributeSet?) {
        attrs?.let(::initFromAttrs)
        setOnCheckedChangeListener { _, _ ->
            val isValid = validate()
            checkboxListeners.forEach { it.onFieldValidityChanged(this, isValid) }
        }
    }

    private fun initFromAttrs(attributeSet: AttributeSet) {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.ValidatableCheckbox, 0, 0)
        isRequired = attrs.getBoolean(R.styleable.ValidatableCheckbox_required, false)
        errorMessage = attrs.getString(R.styleable.ValidatableCheckbox_errorMessage)
        attrs.recycle()
    }


    override fun validate(showError: Boolean): Boolean {
        val isValid = validator.validate(isChecked)
        if (showError && !isValid) {
            showError(errorMessage ?: formErrorMessageResolver.resolveValidatorErrorMessage(validator))
        }
        return isValid
    }

    override fun addFieldValidListener(listenerValidatable: ValidatableFieldListener) {
        checkboxListeners.add(listenerValidatable)
    }

    override fun showError(message: String) {
        formErrorMessageHandler?.onFieldError(this, message)
    }

    override fun clearError() {

    }

    override fun clear() {
        isChecked = false
    }
}
package com.nodesagency.formvalidator

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import com.google.android.material.textfield.TextInputEditText
import com.nodesagency.formvalidator.base.FieldValidChangeListener
import com.nodesagency.formvalidator.base.Validatable
import com.nodesagency.formvalidator.utils.Logger
import com.nodesagency.formvalidator.validators.EmailValidator
import com.nodesagency.formvalidator.validators.NumberValidator
import com.nodesagency.formvalidator.validators.TextInputValidator


class ValidatableEditText : TextInputEditText, Validatable {

    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var validator: TextInputValidator
    private val listeners: MutableList<FieldValidChangeListener> = mutableListOf()
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            listeners.forEach { it.onFieldValidityChanged(this@ValidatableEditText, validate()) }
        }
    }

    init {
        addTextChangedListener(textWatcher)
        validator = getValidatorFromInputType()
        Logger.log("Active validator: ${validator.javaClass.simpleName}")
    }

    private fun getValidatorFromInputType() : TextInputValidator {
        return when (inputType) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> EmailValidator()
            InputType.TYPE_CLASS_NUMBER -> NumberValidator()
            else -> TextInputValidator()
        }
    }



    override fun validate(): Boolean {
        return validator.validate(text.toString())
    }

    override fun addFieldValidListener(listener: FieldValidChangeListener) {
        listeners.add(listener)
    }
}

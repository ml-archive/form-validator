package com.nodesagency.formvalidator

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import com.google.android.material.textfield.TextInputEditText
import com.nodesagency.formvalidator.base.FieldValidChangeListener
import com.nodesagency.formvalidator.base.Validatable
import com.nodesagency.formvalidator.validators.TextValidator


class ValidatableEditText : TextInputEditText, Validatable {

    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val listeners: MutableList<FieldValidChangeListener> = mutableListOf()
    private val validator = TextValidator()
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            listeners.forEach { it.onFieldValidityChanged(validate()) }
        }
    }

    init {
        init()
    }


    private fun init() {
        addTextChangedListener(textWatcher)
    }

    override fun validate(): Boolean {
        return validator.validate(text.toString())
    }

    override fun addFieldValidListener(listener: FieldValidChangeListener) {
        listeners.add(listener)
    }
}

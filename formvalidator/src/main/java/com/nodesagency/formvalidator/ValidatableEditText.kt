package com.nodesagency.formvalidator

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.ViewParent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nodesagency.formvalidator.base.FieldValidChangeListener
import com.nodesagency.formvalidator.base.Validatable
import com.nodesagency.formvalidator.utils.Logger
import com.nodesagency.formvalidator.validators.EmailValidator
import com.nodesagency.formvalidator.validators.NumberValidator
import com.nodesagency.formvalidator.validators.TextInputValidator


class ValidatableEditText : TextInputEditText, Validatable, TextView.OnEditorActionListener {

    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        attrs?.let(this::initFromAttributes)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrs?.let(this::initFromAttributes)
    }

    private var validator: TextInputValidator
    private val listeners: MutableList<FieldValidChangeListener> = mutableListOf()

    private var isValid: Boolean = false

    private var textInputLayout: TextInputLayout? = null

    private val textWatcher = object : TextWatcher {

        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val validated = validate()
            if (validated != isValid) {
                isValid = validated
                listeners.forEach { it.onFieldValidityChanged(this@ValidatableEditText, validated) }
            }
        }
    }

    init {
        Logger.log("Init{}")
        addTextChangedListener(textWatcher)
        setOnEditorActionListener(this)
        validator = getValidatorFromInputType()
        Logger.log("Active validator: ${validator.javaClass.simpleName}")

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        textInputLayout = findTextInputLayout(parent)
    }

    override fun onEditorAction(tv: TextView?, actionId: Int, keyEvent: KeyEvent?): Boolean {
       // User is done with this field, validate the field and show if the input is valid
       if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
           Logger.log("Validate and inform")
           validateAndInform()
       }
        return false
    }


    override fun validate(): Boolean {
        return validator.validate(text?.toString() ?: "")
    }

    override fun addFieldValidListener(listener: FieldValidChangeListener) {
        listeners.add(listener)
    }


    private fun initFromAttributes(attributeSet: AttributeSet) {
        Logger.log("InitWith Attsrs")
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.ValidatableEditText, 0, 0)
        attrs.recycle()
    }

    private fun findTextInputLayout(parent: ViewParent) : TextInputLayout? {
       return when {
           parent is TextInputLayout -> return parent
           parent.parent == null -> return null
           else -> findTextInputLayout(parent.parent)
       }
    }

    private fun validateAndInform() {
        if (!validate()) {
            // Let TextInputLayout Display the error
            if (textInputLayout != null) {
                Logger.log("Eror")
                textInputLayout?.error = "Invalid field"
            }
        }
    }

    private fun getValidatorFromInputType(): TextInputValidator {
        return when (inputType) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> EmailValidator()
            InputType.TYPE_CLASS_NUMBER -> NumberValidator()
            else -> TextInputValidator()
        }
    }

}

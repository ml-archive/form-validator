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
import com.nodesagency.formvalidator.base.ErrorMessageHandler
import com.nodesagency.formvalidator.base.ErrorMessageListener
import com.nodesagency.formvalidator.base.ValidatableFieldListener
import com.nodesagency.formvalidator.base.Validatable
import com.nodesagency.formvalidator.utils.Logger
import com.nodesagency.formvalidator.utils.onTextChanged
import com.nodesagency.formvalidator.validators.*
import com.nodesagency.formvalidator.validators.password.PasswordValidator
import com.nodesagency.formvalidator.validators.password.PasswordStreinght


class ValidatableEditText : TextInputEditText, Validatable, TextView.OnEditorActionListener {

    constructor(context: Context) : super(context, null) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    /**
     * Main content validator
     */
    var validator: TextInputValidator = defaultValidator()


    /**
     * Specifies if the input is required for this field
     */
    var isRequired: Boolean = false
        set(value) {
            field = value
            requiredValidator = RequiredValidator(isRequired)
        }


    var requiredMessage: String? = null
    var errorMessage: String? = null

    override var errorMessageHandler: ErrorMessageHandler = DefaultErrorMessageHandler(context)

    override var errorMessageListener: ErrorMessageListener? = null

    var isValid: Boolean = false
        private set(value) {
            field = value
        }

    var passwordStreinght: PasswordStreinght = PasswordStreinght.Weak


    private var identicalTo: Int = 0
    private var requiredValidator: TextInputValidator = defaultValidator()
    private val listenerValidatables: MutableList<ValidatableFieldListener> = mutableListOf()


    private var textInputLayout: TextInputLayout? = null

    private val textWatcher = object : TextWatcher {

        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val validated = validate()
            Logger.log("Validate: $validated")
            clearError()
            if (validated != isValid) {
                isValid = validated
                listenerValidatables.forEach { it.onFieldValidityChanged(this@ValidatableEditText, validated) }

            }
        }
    }


    private fun init(attrs: AttributeSet?) {
        attrs?.let(this::initFromAttributes)
        addTextChangedListener(textWatcher)
        setOnEditorActionListener(this)
        validator = getValidatorFromInputType()
        Logger.log("Active Validator $validator")
    }

    private fun initFromAttributes(attributeSet: AttributeSet) {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.ValidatableEditText, 0, 0)

        val passwordStreinghtInt = attrs.getInt(R.styleable.ValidatableEditText_passwordStreinght, 0)

        isRequired = attrs.getBoolean(R.styleable.ValidatableEditText_required, false)
        identicalTo = attrs.getResourceId(R.styleable.ValidatableEditText_identicalTo, 0)

        errorMessage = attrs.getString(R.styleable.ValidatableEditText_errorMessage)
        requiredMessage = attrs.getString(R.styleable.ValidatableEditText_requiredMessage)

        passwordStreinght = PasswordStreinght.values()[passwordStreinghtInt]

        attrs.recycle()
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        textInputLayout = findTextInputLayout(parent)
        val identicalEditText = if (identicalTo != 0) findEditText(parent, identicalTo) else null

        // Identical Edit Text is provided, change validator and add a listener
        if (identicalEditText != null) {
            validator = IdenticalValidator(identicalEditText)
            identicalEditText.onTextChanged { validate() }
        }
    }

    override fun onEditorAction(tv: TextView?, actionId: Int, keyEvent: KeyEvent?): Boolean {
        // User is done with this field, validate the field and show if the input is valid
        if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
            listenerValidatables.forEach { it.onInputConfirmed(this) }
        }
        return false
    }


    override fun validate(showError: Boolean): Boolean {
        val text = text?.toString() ?: ""

        val requirementValidated = requiredValidator.validate(text)
        val contentValidated = validator.validate(text)

        if (showError) {
            // Check if field is required first
            if (!requirementValidated) {
                val message = requiredMessage ?: errorMessageHandler.handleTextValidatorError(requiredValidator)
                showError(message)
                return false
            }

            // Continue with the primary validator
            if (!contentValidated) {
                val message = errorMessage ?: errorMessageHandler.handleTextValidatorError(validator)
                showError(message)
                return false
            }
        }

        return requirementValidated && contentValidated
    }

    override fun addFieldValidListener(listenerValidatable: ValidatableFieldListener) {
        listenerValidatables.add(listenerValidatable)
    }


    override fun showError(message: String) {
        errorMessageListener?.onError(this, message)
        textInputLayout?.error = message
    }

    override fun clearError() {
        textInputLayout?.error = null
    }


    override fun clear() {
        textInputLayout?.error = null
        text?.clear()
    }

    private fun getValidatorFromInputType(): TextInputValidator {
        return when (inputType) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> EmailValidator()
            InputType.TYPE_CLASS_NUMBER -> NumberValidator()
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD -> PasswordValidator(passwordStreinght)
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME -> NameValidator()
            else -> defaultValidator()
        }
    }


    private fun findTextInputLayout(parent: ViewParent): TextInputLayout? {
        return when {
            parent is TextInputLayout -> return parent
            parent.parent == null -> return null
            else -> findTextInputLayout(parent.parent)
        }
    }


    private fun findEditText(viewGroup: ViewParent, id: Int): ValidatableEditText? {
        return if (viewGroup is ViewGroup) {
            val editText: ValidatableEditText? = viewGroup.findViewById(id)
            val parent = viewGroup.parent as? ViewGroup?
            when {
                editText != null -> editText
                parent != null -> findEditText(parent, id)
                else -> null
            }
        } else null
    }

    private fun defaultValidator() = RequiredValidator(false)
}

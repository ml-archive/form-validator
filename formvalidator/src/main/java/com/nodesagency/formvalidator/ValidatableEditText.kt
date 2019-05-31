package com.nodesagency.formvalidator

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nodesagency.formvalidator.base.FormErrorMessageResolver
import com.nodesagency.formvalidator.base.FormErrorMessageHandler
import com.nodesagency.formvalidator.base.ValidatableFieldListener
import com.nodesagency.formvalidator.base.Validatable
import com.nodesagency.formvalidator.utils.DefaultErrorMessagesResolver
import com.nodesagency.formvalidator.utils.Logger
import com.nodesagency.formvalidator.utils.onTextChanged
import com.nodesagency.formvalidator.validators.*
import com.nodesagency.formvalidator.validators.password.PasswordValidator
import com.nodesagency.formvalidator.validators.password.PasswordStreinght


class ValidatableEditText : TextInputEditText, Validatable<String>, TextView.OnEditorActionListener, View.OnFocusChangeListener {

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
    override var isRequired: Boolean = false
        set(value) {
            field = value
            requiredValidator = RequiredValidator(isRequired)
        }


    /**
     * Error message that will be show when the field is required, but the input is empty
     * When not specified formErrorMessageResolver will be used to resolve the error message
     */
    var requiredMessage: String? = null

    /**
     * Error message that will be shown when the input doesn't pass the main validator check (i.e email format)
     * When not specified formErrorMessageResolver will be used to resolve the error message ( depending on validator type)
     */
    var errorMessage: String? = null

    override var formErrorMessageResolver: FormErrorMessageResolver =
        DefaultErrorMessagesResolver(context)

    override var formErrorMessageHandler: FormErrorMessageHandler? = null

    /**
     * Tells if field is valid
     */
    var isValid: Boolean = false
        private set(value) {
            field = value
        }

    /**
     * Represents the password streinght
     * Used when password validator is set
     */
    var passwordStreinght: PasswordStreinght = PasswordStreinght.None


    private var identicalTo: Int = 0
    private var requiredValidator: TextInputValidator = defaultValidator()
    private val validatableListeners: MutableList<ValidatableFieldListener> = mutableListOf()


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
                validatableListeners.forEach { it.onFieldValidityChanged(this@ValidatableEditText, validated) }

            }
        }
    }


    private fun init(attrs: AttributeSet?) {
        attrs?.let(this::initFromAttributes)
        addTextChangedListener(textWatcher)
        setOnEditorActionListener(this)
        onFocusChangeListener = this
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
            validatableListeners.forEach { it.onInputConfirmed(this) }
        }
        return false
    }

    override fun onFocusChange(view: View?, focused: Boolean) {
        if (!focused) {
            validatableListeners.forEach { it.onInputConfirmed(this) }
        }
    }

    override fun validate(showError: Boolean): Boolean {
        val text = text?.toString() ?: ""

        val requirementValidated = requiredValidator.validate(text)
        val contentValidated = validator.validate(text)

        if (showError) {
            // Check if field is required first
            if (!requirementValidated) {
                val message = requiredMessage ?: formErrorMessageResolver.resolveValidatorErrorMessage(requiredValidator)
                showError(message)
                return false
            }

            // Continue with the primary validator
            if (!contentValidated) {
                val message = errorMessage ?: formErrorMessageResolver.resolveValidatorErrorMessage(validator)
                showError(message)
                return false
            }
        }

        return requirementValidated && contentValidated
    }

    override fun addFieldValidListener(listenerValidatable: ValidatableFieldListener) {
        validatableListeners.add(listenerValidatable)
    }


    override fun value(): Pair<Int, String?> {
        return id to text?.toString()
    }

    override fun showError(message: String) {
        formErrorMessageHandler?.onFieldError(this, message)
        textInputLayout?.error = message
    }

    override fun clearError() {
        textInputLayout?.error = null
    }


    override fun clear() {
        clearError()
        text?.clear()
    }


    /**
     * Set error handler for this view specifically
     * @param block action that will be triggered
     */
    fun setErrorHandler(block: (String) -> Unit) {
        formErrorMessageHandler = object : FormErrorMessageHandler {
            override fun onFieldError(view: View, message: String) {
                block.invoke(message)
            }
        }
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

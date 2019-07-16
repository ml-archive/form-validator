package dk.nodes.formvalidator

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.nodes.formvalidator.base.*
import com.nodes.formvalidator.utils.asSequence
import com.nodes.formvalidator.utils.Action
import com.nodes.formvalidator.utils.Logger


class FormLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0) :
    FrameLayout(context, attributeSet, defStyle), ValidatableFieldListener {


    /**
     * indicates whether form should automatically display all the errors
     */
    var errorHandlerMode: ErrorHandlerMode = ErrorHandlerMode.Ime



    private var listener: FormLayoutListener? = null
    private lateinit var validatableViews: List<Validatable>
    private var childrenResolved: Boolean = false
    private var pendingActions = ArrayList<Action>()


    init {
        init(attributeSet)
    }


    private fun init(attributeSet: AttributeSet?) {
        attributeSet?.let(this::initFromAttributes)
    }

    private fun initFromAttributes(attributeSet: AttributeSet) {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.FormLayout)
        val modeInt = attrs.getInt(R.styleable.FormLayout_errorHandling, 0)
        errorHandlerMode = ErrorHandlerMode.values()[modeInt]
        attrs.recycle()
    }



    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (!childrenResolved) {
            validatableViews = resolveValidatableChildren(this)
            validatableViews.forEach { it.addFieldValidListener(this) }

            pendingActions.forEach { it.invoke() }
            pendingActions.clear()

            childrenResolved = true
            Logger.log("Form Inputs count: ${validatableViews.count()}")
        }
    }

    override fun onFieldValidityChanged(validatable: Validatable, isValid: Boolean) {
        val allValid = validatableViews.all { it.validate() }
        listener?.onFormValidityChanged(allValid)
    }

    override fun onInputConfirmed(validatable: Validatable) {
        if (errorHandlerMode == ErrorHandlerMode.Ime) {
            validatable.validate(true)
        }
    }

    override fun onInputLostFocus(validatable: Validatable) {
        if (errorHandlerMode == ErrorHandlerMode.Focus) {
            validatable.validate(true)
        }
    }


    /**
     * Allows to react when the form validity status changes
     * @param block - action that will be triggered when status changes
     */
    fun setFormValidListener(block: (Boolean) -> Unit) {
        listener = object : FormLayoutListener {
            override fun onFormValidityChanged(isValid: Boolean) {
                block.invoke(isValid)
            }
        }
    }

    /**
     * Allows to specify error messages resolver for all the validatable views in the form
     * @param formErrorMessageResolver - custom resolver implementation
     */
    fun setErrorMessageResolver(formErrorMessageResolver: FormErrorMessageResolver) {
        postChildrenAction { validatableViews.forEach { it.formErrorMessageResolver = formErrorMessageResolver } }
    }

    /**
     * Allows to specify an error handler for all the validatable views in the form
     * @param formErrorMessageHandler - custom handler implementation
     */
    fun setErrorMessagesHandler(formErrorMessageHandler: FormErrorMessageHandler) {
        postChildrenAction { validatableViews.forEach { it.formErrorMessageHandler = formErrorMessageHandler } }
    }

    /**
     * Validates all children and displays the errors
     * @return true if all fields are valid
     */
    fun validateAll(): Boolean {
        return if (!childrenResolved) false
        else validatableViews
            .also { it.forEach { it.clearError() } }
            .map { it.validate(true) }
            .all { it }
    }

    /**
     * Restores all the fields state from bundle
     * @see Bundlable
     * @param bundle - bundle with saved state
     */
    fun restoreFromBundle(bundle: Bundle) {
        postChildrenAction {
            validatableViews.mapNotNull { it as? Bundlable }.forEach {
                it.restoreFromBundle(bundle)
            }
        }
    }

    /**
     * Bundles all the fields that implements Bundlable
     * @see Bundlable
     * @return bundle with the saved state
     */
    fun retrieveAsBundle() : Bundle {
        return if (childrenResolved) {
            val bundles = validatableViews.mapNotNull { it as? Bundlable }.map { it.storeToBundle() }
            Bundle().apply { bundles.forEach { putAll(it) } }
        } else Bundle()
    }

    /**
     * Obtain all field values
     * @return map with id:value pairs
     */
    fun retrieveAll() : Map<Int, Any?> {
        return validatableViews.map { it.value() }.toMap()
    }

    /**
     * Clear all form fields and errors
     */
    fun clear() {
        for (view in validatableViews) {
            view.clear()
        }
    }

    /**
     * Finds all children in the hierarchy that implement Validatable
     * @return list of Validatable from this hierarchy
     */
    private fun resolveValidatableChildren(viewGroup: ViewGroup): List<Validatable> {
        return viewGroup.asSequence().map {
            when (it) {
                is Validatable -> listOf<Validatable>(it)
                is ViewGroup -> resolveValidatableChildren(it)
                else -> listOf()
            }
        }.toList().flatten()
    }



    private fun postChildrenAction(action: Action) {
        if (!childrenResolved) {
            pendingActions.add(action)
        } else {
            action.invoke()
        }
    }

    /**
     * Enum to handle when the errors are gonna be displayed
     */
    enum class ErrorHandlerMode {

        /**
         * Error, if any, will be shown after input confirmation, i.e IME action
         */
        Ime,


        /**
         * Error, if any, will be shown when the input loses focus
         */
        Focus,


        /**
         * Error will not be shown until validateAll() is triggered
         */
        Manual
    }


}
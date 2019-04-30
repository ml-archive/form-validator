package com.nodesagency.formvalidator

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.nodesagency.formvalidator.utils.asSequence
import com.nodesagency.formvalidator.base.FieldValidChangeListener
import com.nodesagency.formvalidator.base.FormValidListener
import com.nodesagency.formvalidator.base.Validatable
import com.nodesagency.formvalidator.utils.Logger

class FormLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0) : FrameLayout(context, attributeSet, defStyle), FieldValidChangeListener {

    private lateinit var validatableViews: List<Validatable>
    private var childrenResolved: Boolean = false


    var listener: FormValidListener? = null

    fun setFormValidListener(block: (Boolean) -> Unit) {
        listener = object : FormValidListener {
            override fun onFormValidityChanged(isValid: Boolean) {
                block.invoke(isValid)
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (!childrenResolved) {
            validatableViews = resolveValidatableChildren(this)
            validatableViews.forEach { it.addFieldValidListener(this) }
            childrenResolved = true
            Logger.log("Form Inputs count: ${validatableViews.count()}")
        }
    }

    override fun onFieldValidityChanged(validatable: Validatable, isValid: Boolean) {
        val allValid = validatableViews.all { it.validate() }
        listener?.onFormValidityChanged(allValid)
    }

    private fun resolveValidatableChildren(viewGroup: ViewGroup) : List<Validatable> {
        return viewGroup.asSequence().map {
            when(it) {
                is Validatable -> listOf<Validatable>(it)
                is ViewGroup -> resolveValidatableChildren(it)
                else -> listOf()
            }}.toList().flatten()
    }




}
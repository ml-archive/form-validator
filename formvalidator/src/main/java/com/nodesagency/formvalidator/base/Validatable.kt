package com.nodesagency.formvalidator.base

interface Validatable  {

    fun validate() : Boolean
    fun addFieldValidListener(listener: FieldValidChangeListener)
}
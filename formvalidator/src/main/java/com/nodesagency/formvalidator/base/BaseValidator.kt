package com.nodesagency.formvalidator.base

interface BaseValidator<in T>  {
    fun validate(value: T) : Boolean
}
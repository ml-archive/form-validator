package com.nodesagency.formvalidator.validators

class RequiredValidator: TextInputValidator(){
    override fun validate(value: String): Boolean {
        return value.isNotBlank()
    }
}
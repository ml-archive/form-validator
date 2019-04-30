package com.nodesagency.formvalidator.utils

object Password {
    const val Medium = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}\$"
    const val Strong = "(?=^.{8,}$)(?=.*\\d)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$"
}
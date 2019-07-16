package dk.nodes.formvalidator.utils

object RegexPatterns {


    object Password {
        const val Medium = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}\$"
        const val Strong = "(?=^.{8,}$)(?=.*\\d)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$"
    }


    const val Name = "([A-Z][a-zA-Z]*)+( [A-Z][a-zA-Z]*)*"

}
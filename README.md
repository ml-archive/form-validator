# Form Validator
Library to handle validation of input fields.

## Features:
  - Out of the box validation for common input types such as passwords, emails and Names.
  - Grouping input fields into Forms to centrilize validation logic
  - Automatic error handling when used together with `TextInputLayout`



## ValidatableEditText
`ValidatableEditText` allows to validate one input field. It is a subclass of the `TextInputEditText` from the material making it easy to use with the `TextInputLayout`. By specifying the `inputType`, it will try to automatically configure the validation logic. Currently supported `inputTypes`:
  - `textEmailAdrres`
  - `textPersonName`
  - `number`
  - `textPassword`


### Attributes

  | Attribute | Description | Default |
  | --- | --- | --- |
  | `app:passwordStreinght` | When `inputType` set to `textPassword`, this Attribute can further modify how strong the password must be. Can either be set as `weak`, `medium` or `strong` | `weak` |
  | `app:identicalTo` | Allows to reference another `ValidatableEditText`, telling the view that its input should match to the referenced view's input  | `0` |
  | `app:identicalTo` | Allows to reference another `ValidatableEditText`, telling the view that its input should match to the referenced view's input  | `none` |
  | `app:required` | Specifies if the input is required  | `false` |

#### Example

##### Layout declaration
```xml
<com.google.android.material.textfield.TextInputLayout

               android:layout_width="match_parent"
               android:layout_height="wrap_content"
               android:hint="Password">

               <com.nodesagency.formvalidator.ValidatableEditText
                   android:id="@+id/passwordRepeatEt"
                   app:required="true"
                   app:identicalTo="@id/passwordEt"
                   android:inputType="textPassword"
                   android:layout_width="match_parent"
                   android:layout_height="match_parent" />

</com.google.android.material.textfield.TextInputLayout>
 ```

##### In Activity/Fragments

```kotlin
// specify a custom Validator
editText1.required = true
editText1.validator = object : TextInputValidator() {
            override fun validate(value: String): Boolean {
                return value == "hello"
            }
        }

```



## FormLayout
`FormLayout` serves as a container for `ValidatableEditText` to centrilize its validation.

### Attrubutes

| Attribute | Description | Default |
| --- | --- | --- |
| `app:errorHandling` | Tells layout when its children should display the error in input. `manual` mode will show errors only when it is requested, `automatic` will show error, if any, when the input is confirmed (i.e IME action)  | `automatic` |

### Example

#### Layout declaration

```xml
<com.nodesagency.formvalidator.FormLayout
       android:id="@+id/form2"
       app:errorHandling="manual"
       android:layout_width="match_parent"
       android:layout_height="wrap_content">

      <!-- your layout definition -->

           <com.google.android.material.textfield.TextInputLayout
               android:layout_width="match_parent"
               android:layout_height="wrap_content"
               android:hint="email">

               <com.nodesagency.formvalidator.ValidatableEditText
                   android:id="@+id/editText1"
                   app:required="true"
                   android:inputType=""
                   android:layout_width="match_parent"
                   android:layout_height="match_parent" />

           </com.google.android.material.textfield.TextInputLayout>

           <com.google.android.material.textfield.TextInputLayout

               android:layout_width="match_parent"
               android:layout_height="wrap_content"
               android:hint="Password">

               <com.nodesagency.formvalidator.ValidatableEditText
                   android:id="@+id/passwordEt"
                   app:required="true"
                   android:inputType="textPassword"
                   android:layout_width="match_parent"
                   android:layout_height="match_parent" />

           </com.google.android.material.textfield.TextInputLayout>

</com.nodesagency.formvalidator.FormLayout>


```

#### In Activity/Fragments
```kotlin
// Form state listener
form.setFormValidListener { isFormValid ->
          // update ui to reflect that
          loginButton.isEnabled = isFormValid
       }

// Custom error messages
form.setFormErrorHandler(object : ErrorMessageHandler {
    override fun handleTextValidatorError(textInputValidator: TextInputValidator): String {
       if (textInputValidator is EmailValidator) {
         return "Custom Email Message"
       }
    }
})
```

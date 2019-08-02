# Whats new
  - Error messages can be provided as attributes
  - Ability to provide custom action when validation error occurs
  - Better Lib Examples
  - Clear form functionality
  - ValidatableCheckBox
  - Validate edit text when losing focus
  - `form.retrieveAll()` - to retrieve all the field values from the form as Map
  - "None" password strength when its better to skip validation
  - `Bundlable` interface to help with configuration changes (store/restore Form state with bundles)


# Form Validator
Library to handle validation of input fields.

## Installation

```groovy
    implementation 'dk.nodes.formvalidator:base:1.0.0'
```

## Features:
  - Out of the box validation for common input types such as passwords, emails and Names.
  - Grouping input fields into Forms to centralise validation logic
  - Automatic error handling when used together with `TextInputLayout`
  - Storing and restoring form's state



## ValidatableEditText
`ValidatableEditText` allows to validate one input field. It is a subclass of the `TextInputEditText` from the material library making it easy to use with the `TextInputLayout`. By specifying the `inputType`, it will try to automatically configure the validation logic. Currently supported `inputTypes`:
  - `textEmailAddress`
  - `textPersonName`
  - `number`
  - `textPassword`


### Attributes

  | Attribute | Description | Default |
  | --- | --- | --- |
  | `app:passwordStrength` | When `inputType` set to `textPassword`, this Attribute can further modify how strong the password must be. Can either be set as `none`, `weak`, `medium` or `strong` | `none` |
  | `app:identicalTo` | Allows to reference another `ValidatableEditText`, telling the view that its input should match to the referenced view's input  | `0` |
  | `app:errorMessage` | Specifies the error message to be shown when content is invalid, when not specified `FormErrorMessageResolver` will be used to resolve the message base on the validator used  | `` |
| `app:requiredMessage` | Specifies the error message to be shown when content is empty but is required, when not specified `FormErrorMessageResolver` will be used to resolve the message| `` |
  | `app:required` | Specifies if the input is required  | `false` |
  | `app:min` | Specifies the minimal allowed length for this input field.  | `null` |


#### Example

##### Layout declaration
```xml
<com.google.android.material.textfield.TextInputLayout

               android:layout_width="match_parent"
               android:layout_height="wrap_content"
               android:hint="Password">

               <dk.nodes.formvalidator.ValidatableEditText
                   android:id="@+id/passwordRepeatEt"
                   app:required="true"
                   app:min="10"
                   app:identicalTo="@id/passwordEt"
                   android:inputType="textPassword"
                   android:layout_width="match_parent"
                   android:layout_height="match_parent" />

</com.google.android.material.textfield.TextInputLayout>
 ```

##### In Activity/Fragments
You can also specify some of the attribues using kotlin code
```kotlin
// specify a custom Validator
editText1.required = true
editText1.errorMessage = "My error message"
editText1.min = 10
#### Custom Validation
editText1.validator = object : TextInputValidator() {
            override fun validate(value: String): Boolean {
                return value == "hello"
            }
        }

```


## ValidatableCheckBox
`ValidatableCheckBox` is another validatable field. It has only one parameter: whether it requires to be checked or not
### Attributes

  | Attribute | Description | Default |
  | --- | --- | --- |
  | `app:required` | Specifies if the input is required  | `false` |


## FormLayout
`FormLayout` serves as a container for `ValidatableEditText` and `ValidatableCheckBox` to centrilize the validation.

### Attrubutes

| Attribute | Description | Default |
| --- | --- | --- |
| `app:errorHandling` | Tells layout when its children should display the input error. `manual` mode will show errors only when it is requested, `ime` will show the error, if any, when the input is confirmed (i.e IME action). `focus` will show the error when the field loses focus  | `ime` |

### Example

#### Layout declaration

```xml
<dk.nodes.formvalidator.FormLayout
       android:id="@+id/form2"
       app:errorHandling="manual"
       android:layout_width="match_parent"
       android:layout_height="wrap_content">

      <!-- your layout definition -->

           <com.google.android.material.textfield.TextInputLayout
               android:layout_width="match_parent"
               android:layout_height="wrap_content"
               android:hint="email">

               <dk.nodes.formvalidator.ValidatableEditText
                   android:id="@+id/editText1"
                   app:required="true"
                   android:inputType="textEmailAdress"
                   android:layout_width="match_parent"
                   android:layout_height="match_parent" />

           </com.google.android.material.textfield.TextInputLayout>

           <com.google.android.material.textfield.TextInputLayout

               android:layout_width="match_parent"
               android:layout_height="wrap_content"
               android:hint="Password">

               <dk.nodes.formvalidator.ValidatableEditText
                   android:id="@+id/passwordEt"
                   app:required="true"
                   android:inputType="textPassword"
                   android:layout_width="match_parent"
                   android:layout_height="match_parent" />

           </com.google.android.material.textfield.TextInputLayout>

</dk.nodes.formvalidator.FormLayout>


```

#### In Activity/Fragments



#### Form validation
In case you want to manually validate the form, you can call `validateAll()` method.
This will check validity of all validatable fields and, in case of error, will trigger `showError()` for respective fields.

```kotlin
loginBtn.setOnClickListener {
  if (form.validateAll()) {
    // all good
  } else {
    // there are some errors
  }
}
```

#### Retrieving values
You can retrieve all the values from the fields as map using `retrieveAll()` methods. It will map values to the respective views ID, so make sure you have distinct IDs for your fields.
```kotlin
 fun form.getSignupData() : SignupRequest {
        val values = retrieveAll()
        return SignupRequest(
            email = values[R.id.emailEt] as String,
            password = values[R.id.passwordEt] as String,
            terms = values[R.id.termsCheckBox] as Boolean
        )
    }
```

#### Store/Restore Form's state
Both `ValidatableEditText` and `ValidatableCheckBox` values can be retrieved as `Bundle`. `FormLayout` provides methods to put all the values from fields into one `Bundle`, so it is use in `onSaveInstanceState` for example
```kotlin
// Store state
override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val bundle = signupForm.retrieveAsBundle()
        outState.putAll(bundle)
    }

// Restoring state
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view, savedInstanceState)
       if (savedInstanceState != null) {
           signupForm.restoreFromBundle(savedInstanceState)
       }
   }
```
> **Warning: `retrieveAsBundle() `and` restoreFromBundle()` relies on views IDs when storing and restoring values, so make sure all your validatable fields have distinct IDs**


#### Listening to changes
You can also set a listener to check when form becomes valid/invalid

```kotlin
// Form state listener
form.setFormValidListener { isFormValid ->
          // update ui to reflect that
          loginButton.isEnabled = isFormValid
       }

```
#### Handling Errors
It is possible to specify custom error handler and message resolver globally for all the views
```kotlin
// Custom error messages
form.setErrorMessagesHandler(object : FormErrorMessageHandler {
    override fun onFieldError(view: View, message: String)   
          // do something with the error received
       }
    }
})
form.setErrorMessageResolver(object: FormErrorMessageResolver {
  override fun resolveValidatorErrorMessage(validator: BaseValidator<*>) : String {
    return "Your custom message"
  }
})
```

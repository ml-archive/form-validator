package com.nodesagency.formvalidator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.nodesagency.formvalidator.base.ErrorMessageHandler
import com.nodesagency.formvalidator.base.ErrorMessageListener
import com.nodesagency.formvalidator.validators.EmailValidator
import com.nodesagency.formvalidator.validators.TextInputValidator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ErrorMessageListener {
    override fun onError(view: View, message: String) {
        Toast.makeText(this, "id $view message: $message", Toast.LENGTH_SHORT).show();
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*form.setFormValidListener {
            button.isEnabled = it
            button.text = "$it"
        }
*/

        button.setOnClickListener { form.validateAll() }
        createBtn.setOnClickListener {
            if (form2.validateAll()) {
//                Toast.makeText(this, "Creating an account...", Toast.LENGTH_LONG).show()
            } else {
//                Toast.makeText(this, "Some fields are invalid", Toast.LENGTH_SHORT).show()
            }
        }

        /*form.setFormErrorHandler(object : ErrorMessageHandler {
            override fun handleTextValidatorError(textInputValidator: TextInputValidator): String {
                if (textInputValidator is EmailValidator) {
                    return "Custom Email Message"
                }

                return ""
            }
        })*/


        form.setErrorMessageListener(this)

    }
}

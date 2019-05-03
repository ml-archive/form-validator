package com.nodesagency.formvalidator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.nodesagency.formvalidator.base.ErrorMessageHandler
import com.nodesagency.formvalidator.validators.TextInputValidator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        form.setFormValidListener {
            button.isEnabled = it
            button.text = "$it"
        }

        createBtn.setOnClickListener {
            if (form2.validateAll()) {
                Toast.makeText(this, "Creating an account...", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Some fields are invalid", Toast.LENGTH_SHORT).show()
            }
        }

    }
}

package com.nodesagency.formvalidator

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.nodesagency.formvalidator.base.FieldValidChangeListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText.addFieldValidListener(object : FieldValidChangeListener {
            override fun onFieldValidityChanged(isValid: Boolean) {
                editTextStatusTv.text = "Is valid: $isValid"
            }
        })
    }
}

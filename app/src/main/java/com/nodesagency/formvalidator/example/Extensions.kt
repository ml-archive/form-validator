package com.nodesagency.formvalidator.example

import android.widget.Toast
import androidx.fragment.app.Fragment


fun Fragment.showToast(message: String) = Toast.makeText (context,message, Toast.LENGTH_SHORT).show()
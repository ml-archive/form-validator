package com.nodesagency.formvalidator.example

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar


fun Fragment.showToast(message: String) = Toast.makeText (context,message, Toast.LENGTH_SHORT).show()

fun Fragment.showSnackbar(message: String) = Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
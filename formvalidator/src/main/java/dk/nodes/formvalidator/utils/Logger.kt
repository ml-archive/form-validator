package dk.nodes.formvalidator.utils

import android.util.Log

object Logger {
    private const val TAG = "NodesValidator"

    fun log(message: String) {
        Log.d(TAG, message)
    }
}
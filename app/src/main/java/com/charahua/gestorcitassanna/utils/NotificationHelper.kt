package com.charahua.gestorcitassanna.utils

import android.content.Context
import android.widget.Toast

object NotificationHelper {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}

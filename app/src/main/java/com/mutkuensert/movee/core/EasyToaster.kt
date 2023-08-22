package com.mutkuensert.movee.core

import android.content.Context
import android.widget.Toast

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

fun Context.showToastIfNotNull(text: String?, duration: Int = Toast.LENGTH_SHORT) {
    if (text != null) {
        Toast.makeText(this, text, duration).show()
    }
}
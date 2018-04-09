package com.otiasj.muzeifavorites

import android.content.Context
import android.widget.Toast

fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun Context.toast(messageId: Int, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, getString(messageId), duration).show()
}
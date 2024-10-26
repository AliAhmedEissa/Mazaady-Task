package com.mazaady.android_task.util.extention

import android.text.TextUtils
import android.view.View


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}


inline fun View.onDebouncedListener(
    delayInClick: Long = 500L,
    crossinline listener: (View) -> Unit,
) {
    val enableAgain = Runnable { isEnabled = true }
    setOnClickListener {
        if (isEnabled) {
            isEnabled = false
            postDelayed(enableAgain, delayInClick)
            listener(it)
        }
    }
}

fun String?.isEmpty(): Boolean {
    return (TextUtils.isEmpty(this ?: "".trim()) || this!!.trim()
        .equals("", ignoreCase = true) || this.trim().equals("{}", ignoreCase = true) || this.trim()
        .equals("null", ignoreCase = true) || this.trim().equals("undefined", ignoreCase = true))
}

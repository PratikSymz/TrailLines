package com.neu.madcourse.mad_team4_finalproject.utils

import android.content.Context
import android.util.Patterns
import android.widget.Toast

class BaseUtils(context: Context) {
    /* The Activity context using the utilities */
    private val mContext: Context = context

    fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /* Helper method used to display toast messages across the application */
    fun showToast(message: String, toastInterval: Int) {
        Toast.makeText(mContext, message, toastInterval).show()
    }

    /* Helper method to check whether a String is null or empty */
    fun isEmpty(message: String?): Boolean {
        return (message == null || message.isEmpty())
    }
}
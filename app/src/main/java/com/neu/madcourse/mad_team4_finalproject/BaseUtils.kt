package com.neu.madcourse.mad_team4_finalproject

import android.content.Context
import android.content.res.Resources
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

class BaseUtils(context: Context) {
    companion object {
        private const val TYPE_DRAWABLE = "drawable"
    }

    /* The Activity context using the utilities */
    private val mContext: Context = context

    /* Helper methods to apply onClick listeners to child views inside ViewGroups */
    fun applyListener(child: View?, listener: View.OnClickListener) {
        if (child == null) return

        if (child is ViewGroup) {
            applyListener(child, listener)
        } else {
            child.setOnClickListener(listener)
        }
    }

    private fun applyListener(parent: ViewGroup, listener: View.OnClickListener) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child is ViewGroup) {
                applyListener(child, listener)
            } else {
                applyListener(child, listener)
            }
        }
    }


    /* Helper method to extract drawable resource entry name */
    fun extractDrawableEntryName(drawableID: Int): String {
        return Resources.getSystem().getResourceEntryName(drawableID)
    }

    /* Helper method to extract drawable resource ID */
    fun extractDrawableID(drawableIdentifier: String): Int {
        return mContext.resources.getIdentifier(
            drawableIdentifier,
            TYPE_DRAWABLE,
            mContext.packageName
        )
    }

    /* Helper method to validate email pattern */
    fun isValidEmail(email: String): Boolean {
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
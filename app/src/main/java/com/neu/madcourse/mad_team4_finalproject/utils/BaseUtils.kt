package com.neu.madcourse.mad_team4_finalproject.utils

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

    private var states = HashMap<String, String>()

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

    /* Helper method to extract statecode from state name */
    fun extractStateCode(stateName: String): String? {
        return states[stateName]
    }

    fun initStateMap() {
        states["Alabama"] = "AL"
        states["Alaska"] = "AK";
        states["Alberta"] = "AB";
        states["American Samoa"] = "AS";
        states["Arizona"] = "AZ";
        states["Arkansas"] = "AR";
        states["Armed Forces (AE)"] = "AE";
        states["Armed Forces Americas"] = "AA";
        states["Armed Forces Pacific"] = "AP";
        states.put("British Columbia", "BC");
        states.put("California", "CA");
        states.put("Colorado", "CO");
        states.put("Connecticut", "CT");
        states.put("Delaware", "DE");
        states.put("District Of Columbia", "DC");
        states.put("Florida", "FL");
        states.put("Georgia", "GA");
        states.put("Guam", "GU");
        states.put("Hawaii", "HI");
        states.put("Idaho", "ID");
        states.put("Illinois", "IL");
        states.put("Indiana", "IN");
        states.put("Iowa", "IA");
        states.put("Kansas", "KS");
        states.put("Kentucky", "KY");
        states.put("Louisiana", "LA");
        states.put("Maine", "ME");
        states.put("Manitoba", "MB");
        states.put("Maryland", "MD");
        states.put("Massachusetts", "MA");
        states.put("Michigan", "MI");
        states.put("Minnesota", "MN");
        states.put("Mississippi", "MS");
        states.put("Missouri", "MO");
        states.put("Montana", "MT");
        states.put("Nebraska", "NE");
        states.put("Nevada", "NV");
        states.put("New Brunswick", "NB");
        states.put("New Hampshire", "NH");
        states.put("New Jersey", "NJ");
        states.put("New Mexico", "NM");
        states.put("New York", "NY");
        states.put("Newfoundland", "NF");
        states.put("North Carolina", "NC");
        states.put("North Dakota", "ND");
        states.put("Northwest Territories", "NT");
        states.put("Nova Scotia", "NS");
        states.put("Nunavut", "NU");
        states.put("Ohio", "OH");
        states.put("Oklahoma", "OK");
        states.put("Ontario", "ON");
        states.put("Oregon", "OR");
        states.put("Pennsylvania", "PA");
        states.put("Prince Edward Island", "PE");
        states.put("Puerto Rico", "PR");
        states.put("Quebec", "QC");
        states.put("Rhode Island", "RI");
        states.put("Saskatchewan", "SK");
        states.put("South Carolina", "SC");
        states.put("South Dakota", "SD");
        states.put("Tennessee", "TN");
        states.put("Texas", "TX");
        states.put("Utah", "UT");
        states.put("Vermont", "VT");
        states.put("Virgin Islands", "VI");
        states.put("Virginia", "VA");
        states.put("Washington", "WA");
        states.put("West Virginia", "WV");
        states.put("Wisconsin", "WI");
        states.put("Wyoming", "WY");
        states.put("Yukon Territory", "YT");
    }
}
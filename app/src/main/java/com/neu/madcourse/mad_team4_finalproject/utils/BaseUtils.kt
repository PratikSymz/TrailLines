package com.neu.madcourse.mad_team4_finalproject.utils

import android.content.Context
import android.content.res.Resources
import android.util.Patterns
import android.widget.Toast
import com.neu.madcourse.mad_team4_finalproject.R
import com.neu.madcourse.mad_team4_finalproject.models.ProfileTag

class BaseUtils(context: Context) {
    companion object {
        private const val TYPE_DRAWABLE = "drawable"
    }

    /* The Activity context using the utilities */
    private val mContext: Context = context

    /* Helper method to set and retrieve the profile tags */
    fun getProfileTags(): List<ProfileTag> {
        val accountTag = ProfileTag(
            extractDrawableEntryName(R.drawable.ic_profile_tag_account),
            Constants.PROFILE_TAG_ACCOUNT,
            Constants.ACTIVITY_ACCOUNT
        )

        val myTripsTag = ProfileTag(
            extractDrawableEntryName(R.drawable.ic_profile_tag_mytrips),
            Constants.PROFILE_TAG_MY_TRIPS,
            Constants.ACTIVITY_MY_TRIPS
        )

        val savedPlacesTag = ProfileTag(
            extractDrawableEntryName(R.drawable.ic_profile_tag_savedplaces),
            Constants.PROFILE_TAG_SAVED_PLACES,
            Constants.ACTIVITY_SAVED_PLACES
        )

        val settingsTag = ProfileTag(
            extractDrawableEntryName(R.drawable.ic_profile_tag_settings),
            Constants.PROFILE_TAG_SETTINGS,
            Constants.ACTIVITY_SETTINGS
        )

        val complianceTag = ProfileTag(
            extractDrawableEntryName(R.drawable.ic_profile_tag_compliance),
            Constants.PROFILE_TAG_COMPLIANCE,
            Constants.ACTIVITY_COMPLIANCE
        )

        return listOf(accountTag, myTripsTag, savedPlacesTag, settingsTag, complianceTag)
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
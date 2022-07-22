package com.neu.madcourse.mad_team4_finalproject.utils

object Constants {
    /* Firebase Realtime database server key labels */
    object UserKeys {
        const val KEY_TLO = "users"

        object PersonalInfoKeys {
            const val KEY_TLO = "personal_info"

            const val KEY_FIRST_NAME = "first_name"
            const val KEY_LAST_NAME = "last_name"
            const val KEY_EMAIL_ID = "email_id"
            const val KEY_ONLINE_STATUS = "online_status"
            const val KEY_PROFILE_URL = "profile_picture_url"
            const val KEY_PRIVATE_PROFILE = "private_profile"
        }
        object FriendRequests{
            const val KEY_TLO = "friend_request"

            const val KEY_REQUEST_STATUS = "request_status"
        }
    }

    /* Friend Request Status's */
    const val REQUEST_STATUS_SENT = "sent"
    const val REQUEST_STATUS_RECEIVED = "received"

    /* The ProfileActivity tags */
    const val PROFILE_TAG_ACCOUNT = "Account"
    const val PROFILE_TAG_MY_TRIPS = "My Trips"
    const val PROFILE_TAG_SAVED_PLACES = "Saved Places"
    const val PROFILE_TAG_SETTINGS = "Settings"
    const val PROFILE_TAG_COMPLIANCE = "Third-party Licences"

    /* The Activity name tags */
    const val ACTIVITY_ACCOUNT = "AccountActivity"
    const val ACTIVITY_MY_TRIPS = "MyTripsActivity"
    const val ACTIVITY_SAVED_PLACES = "SavedPlacesActivity"
    const val ACTIVITY_SETTINGS = "SettingsActivity"
    const val ACTIVITY_COMPLIANCE = "ComplianceActivity"
}
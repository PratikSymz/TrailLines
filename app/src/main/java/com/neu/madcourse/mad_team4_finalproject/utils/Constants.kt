package com.neu.madcourse.mad_team4_finalproject.utils

object Constants {
    /* The User directory keys -> "users" */
    object UserKeys {
        const val KEY_TLO = "users"

        /* The Personal Info sub-directory keys -> "users/personal_info" */
        object PersonalInfoKeys {
            const val KEY_TLO = "personal_info"

            const val KEY_NAME = "name"
            const val KEY_EMAIL_ID = "email_id"
            const val KEY_ONLINE_STATUS = "online_status"
            const val KEY_PROFILE_URL = "profile_picture_url"
            const val KEY_PRIVATE_PROFILE = "private_profile"
        }

        /* The Friend Requests sub-directory keys -> "users/friend_requests" */
        object FriendRequestKeys {
            const val KEY_TLO = "friend_requests"

            const val KEY_REQUEST_STATUS = "request_status"

            // Friend Request Status's
            const val REQUEST_STATUS_SENT = "sent"
            const val REQUEST_STATUS_RECEIVED = "received"
            const val REQUEST_STATUS_ACCEPTED = "accepted"
            const val REQUEST_STATUS_DENIED = "denied"
        }
    }

    /* The Reviews directory keys -> "reviews" */
    object ReviewKeys {
        const val KEY_TLO = "reviews"

        // The review fields
        const val KEY_RATING = "rating"
        const val KEY_TIMESTAMP = "timestamp"
        const val KEY_TITLE = "title"
        const val KEY_MESSAGE = "message"
        const val KEY_RECOMMEND_STATUS = "recommend_status"
        const val KEY_TRAIL_ID = "trail_id"
        const val KEY_IMAGES = "images"
    }

    /* The Chat directory keys -> "chats" */
    object ChatKeys {
        const val KEY_TLO = "chats"

        object MessageKeys {
            const val KEY_TLO = "messages"

            // Message fields
            const val KEY_FROM = "from"
            const val KEY_CONTENT = "content"
            const val KEY_MESSAGE_ID = "message_id"
            const val KEY_TIMESTAMP = "timestamp"
            const val KEY_DATA_TYPE = "data_type"

            // The data types
            const val DATA_TYPE_TEXT = "text"
        }
    }

    object FirebaseStorageKeys {
        const val KEY_IMAGES = "images"
    }

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

    /* Filter screen constants */
    const val SORT = "Sort"
    const val TOP_RATED = "Top Rated"
    const val CLOSEST = "Closest"
    const val BEST_MATCHED = "Best Matched"
    const val DIFFICULTY = "Difficulty"
    const val EASY = "Easy"
    const val MEDIUM = "Medium"
    const val HARD = "Hard"
    const val LENGTH = "Length"
    const val ELEVATION = "Elevation"
    const val RATING = "Rating"
    const val PREFERENCES = "Preferences"
    const val DOG = "Dog"
    const val KID = "Kid"
    const val WHEELCHAIR = "Wheelchair"
    const val CAMPING = "Camping"
    const val BIKING = "Biking"
    const val LENGTH_START = "lengthStart"
    const val LENGTH_END = "lengthEnd"
    const val ELEVATION_START = "elevationStart"
    const val ELEVATION_END = "elevationEnd"
    const val RATING_START = "ratingStart"
    const val RATING_END = "ratingEnd"



}
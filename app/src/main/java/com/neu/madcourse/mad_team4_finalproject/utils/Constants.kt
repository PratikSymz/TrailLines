package com.neu.madcourse.mad_team4_finalproject.utils

import com.neu.madcourse.mad_team4_finalproject.R

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

    object ThingsToDoImageIds {
        const val CAMPING = R.drawable.camping
        const val BIKING = R.drawable.biking
        const val BOATING = R.drawable.boating
        const val CANYONEERING = R.drawable.canyon
        const val CAVING = R.drawable.caving
        const val CLIMBING = R.drawable.climbing
        const val FISHING = R.drawable.fishing
        const val HIKING = R.drawable.hiking
        const val PADDLING = R.drawable.paddling
        const val SCUBA_DIVING = R.drawable.scuba
        const val SNORKELLING = R.drawable.snorkelling
        const val SKIING = R.drawable.skiing
        const val SURFING = R.drawable.surfing
        const val WATER_SKIING = R.drawable.waterskiing
    }

    object ThingsToDoStrings {
        const val CAMPING = "Camping"
        const val BIKING = "Biking"
        const val BOATING = "Boating"
        const val CANYONEERING = "Canyoneering"
        const val CAVING = "Caving"
        const val CLIMBING = "Climbing"
        const val FISHING = "Fishing"
        const val HIKING = "Hiking"
        const val PADDLING = "Paddling"
        const val SCUBA_DIVING = "SCUBA Diving"
        const val SNORKELING = "Snorkeling"
        const val SKIING = "Skiing"
        const val SURFING = "Surfing"
        const val WATER_SKIING = "Water Skiing"
    }
}
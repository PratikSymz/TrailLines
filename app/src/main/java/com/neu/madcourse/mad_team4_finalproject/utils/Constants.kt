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
            const val KEY_ONLINE = "online"
            const val KEY_OFFLINE = "offline"
        }

        /* The Friend Requests sub-directory keys -> "users" */
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

        /* Sub-directory keys -> "reviews"/"review_stats" */
        object ReviewStats {
            const val KEY_TLO = "review_stats"

            // The Review stats fields
            const val KEY_TOTAL_REVIEWERS = "total_reviewers"
            const val KEY_TOTAL_STARS = "total_stars"
            const val KEY_NUM_EXCELLENT = "num_excellent"
            const val KEY_NUM_GREAT = "num_great"
            const val KEY_NUM_AVERAGE = "num_average"
            const val KEY_NUM_POOR = "num_poor"
            const val KEY_NUM_TERRIBLE = "num_terrible"
        }

        /* Sub-directory keys -> "reviews"/"review_messages" */
        object ReviewMessages {
            const val KEY_TLO = "review_messages"

            // The review message fields
            const val KEY_RATING = "rating"
            const val KEY_TIMESTAMP = "timestamp"
            const val KEY_TITLE = "title"
            const val KEY_MESSAGE = "message"
            const val KEY_RECOMMEND_STATUS = "recommend_status"
            const val KEY_TRAIL_ID = "trail_id"
            const val KEY_IMAGES = "images"
        }
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

            const val UNREAD_COUNT = "unread_message_count"
            const val LAST_MESSAGE = "last_message"
            const val LAST_MESSAGE_TIME = "last_message_time"

            // The data types
            const val DATA_TYPE_TEXT = "text"
        }
    }

    object FirebaseStorageKeys {
        const val KEY_IMAGES = "images"
    }

    // Notification tokens
    object Tokens {
        const val KEY_TLO = "tokens"

        const val DEVICE_TOKEN = "device_token"
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
    const val ACTIVITY_SETTINGS = "AppSettingsPage"
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
        const val ALL = R.drawable.allactivities
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
        const val ALL = "All"
    }

    object ThingsToDoCodes {
        const val CAMPING_CODE = "A59947B7-3376-49B4-AD02-C0423E08C5F7"
        const val BIKING_CODE = "7CE6E935-F839-4FEC-A63E-052B1DEF39D2"
        const val BOATING_CODE = "071BA73C-1D3C-46D4-A53C-00D5602F7F0E"
        const val CANYONEERING_CODE = "07CBCA6A-46B8-413F-8B6C-ABEDEBF9853E"
        const val CAVING_CODE = "BA316D0F-92AE-4E00-8C80-DBD605DC58C3"
        const val CLIMBING_CODE = "B12FAAB9-713F-4B38-83E4-A273F5A43C77"
        const val FISHING_CODE = "AE42B46C-E4B7-4889-A122-08FE180371AE"
        const val HIKING_CODE = "BFF8C027-7C8F-480B-A5F8-CD8CE490BFBA"
        const val PADDLING_CODE = "4D224BCA-C127-408B-AC75-A51563C42411"
        const val SCUBA_DIVING_CODE = "42CF4021-8524-428E-866A-D33097A4A764"
        const val SNORKELING_CODE = "3EBF7EAC-68FC-4754-B6A4-0C38A1583D45"
        const val SKIING_CODE = "F9B1D433-6B86-4804-AED7-B50A519A3B7C"
        const val SURFING_CODE = "AE3C95F5-E05B-4A28-81DD-1C5FD4BE88E2"
        const val WATER_SKIING_CODE = "8A1C7B17-C2C6-4F7C-9539-EA1E19971D80"
    }

    object Retrofit {
        const val BASE_URL = "https://developer.nps.gov/api/v1/"
        const val API_KEY = "gRfVnZYb1bHwKtboVOQUS1kgFpP4243lIiYCY51I"
        const val STATE_MA = "MA"
    }

    object MapKeys {
        const val API_KEY = "AIzaSyBG6sBP1e1UXr-PIY06i7PLV9jmdLy8cIg"
        const val FORMAT =
            "https://maps.googleapis.com/maps/api/staticmap?center=%s,%s&zoom=14&scale=2&size=400x400&maptype=roadmap&key=%s"
    }

    /* JSON object structure for data message notification data */
    const val NOTIFICATION_TITLE = "title"
    const val NOTIFICATION_MESSAGE = "message"
    const val NOTIFICATION_TO = "to"
    const val NOTIFICATION_DATA = "data"

    /* Notification Channels to group all different chat notifications for the app */
    const val CHANNEL_ID = "channel_id"
    const val CHANNEL_NAME = "channel_name"
    const val CHANNEL_DESCRIPTION = "channel_description"

    // firebase cloud messaging key and sender id
    const val FIREBASE_MESSAGING_KEY =
        "BG1_DKwTp7UBPrpSADz7M5mEx9Ra3kYs84X0lZ94wOcgDqWlVOi3VYWOGXZoAXSLF4_C-78kI0YcelSjvK9h6Vk"
    const val FIREBASE_MESSAGE_SENDER_ID = "707222154198"
}

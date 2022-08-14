package com.neu.madcourse.mad_team4_finalproject.models

import com.google.firebase.database.PropertyName
import com.neu.madcourse.mad_team4_finalproject.utils.Constants

data class ReviewMessage(
    var user: User? = null,
    @get:PropertyName(Constants.ReviewKeys.ReviewMessages.KEY_RATING)
    @set:PropertyName(Constants.ReviewKeys.ReviewMessages.KEY_RATING)
    var rating: Float? = 0f,
    @get:PropertyName(Constants.ReviewKeys.ReviewMessages.KEY_TIMESTAMP)
    @set:PropertyName(Constants.ReviewKeys.ReviewMessages.KEY_TIMESTAMP)
    var timestamp: Long? = 0,
    @get:PropertyName(Constants.ReviewKeys.ReviewMessages.KEY_TITLE)
    @set:PropertyName(Constants.ReviewKeys.ReviewMessages.KEY_TITLE)
    var reviewTitle: String? = "",
    @get:PropertyName(Constants.ReviewKeys.ReviewMessages.KEY_MESSAGE)
    @set:PropertyName(Constants.ReviewKeys.ReviewMessages.KEY_MESSAGE)
    var reviewMessage: String? = "",
    @get:PropertyName(Constants.ReviewKeys.ReviewMessages.KEY_RECOMMEND_STATUS)
    @set:PropertyName(Constants.ReviewKeys.ReviewMessages.KEY_RECOMMEND_STATUS)
    var recommendationStatus: Boolean? = null,
    @get:PropertyName(Constants.ReviewKeys.ReviewMessages.KEY_TRAIL_ID)
    @set:PropertyName(Constants.ReviewKeys.ReviewMessages.KEY_TRAIL_ID)
    var trailID: String? = "",
    @get:PropertyName(Constants.ReviewKeys.ReviewMessages.KEY_IMAGES)
    @set:PropertyName(Constants.ReviewKeys.ReviewMessages.KEY_IMAGES)
    var reviewImages: List<String>? = mutableListOf()
)
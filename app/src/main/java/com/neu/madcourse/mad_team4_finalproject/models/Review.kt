package com.neu.madcourse.mad_team4_finalproject.models

import com.google.firebase.database.PropertyName
import com.neu.madcourse.mad_team4_finalproject.utils.Constants

data class Review(
    @PropertyName(Constants.ReviewKeys.KEY_RATING)
    val rating: Float,
    @PropertyName(Constants.ReviewKeys.KEY_TIMESTAMP)
    val timestamp: Long,
    @PropertyName(Constants.ReviewKeys.KEY_TITLE)
    val reviewTitle: String,
    @PropertyName(Constants.ReviewKeys.KEY_MESSAGE)
    val reviewMessage: String,
    @PropertyName(Constants.ReviewKeys.KEY_RECOMMEND_STATUS)
    val recommendationStatus: Boolean,
    @PropertyName(Constants.ReviewKeys.KEY_TRAIL_ID)
    val trailID: String,
    @PropertyName(Constants.ReviewKeys.KEY_IMAGES)
    val reviewImages: List<String>
)

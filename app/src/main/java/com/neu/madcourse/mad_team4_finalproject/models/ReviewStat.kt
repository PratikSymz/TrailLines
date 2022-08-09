package com.neu.madcourse.mad_team4_finalproject.models

import com.google.firebase.database.PropertyName
import com.neu.madcourse.mad_team4_finalproject.utils.Constants

data class ReviewStat(
    @get:PropertyName(Constants.ReviewKeys.ReviewStats.KEY_TOTAL_REVIEWERS)
    @set:PropertyName(Constants.ReviewKeys.ReviewStats.KEY_TOTAL_REVIEWERS)
    var totalReviewers: Long? = 0,
    @get:PropertyName(Constants.ReviewKeys.ReviewStats.KEY_TOTAL_STARS)
    @set:PropertyName(Constants.ReviewKeys.ReviewStats.KEY_TOTAL_STARS)
    var totalStars: Double? = 0.0,
    @get:PropertyName(Constants.ReviewKeys.ReviewStats.KEY_NUM_EXCELLENT)
    @set:PropertyName(Constants.ReviewKeys.ReviewStats.KEY_NUM_EXCELLENT)
    var numExcellent: Long? = 0,
    @get:PropertyName(Constants.ReviewKeys.ReviewStats.KEY_NUM_GREAT)
    @set:PropertyName(Constants.ReviewKeys.ReviewStats.KEY_NUM_GREAT)
    var numGreat: Long? = 0,
    @get:PropertyName(Constants.ReviewKeys.ReviewStats.KEY_NUM_AVERAGE)
    @set:PropertyName(Constants.ReviewKeys.ReviewStats.KEY_NUM_AVERAGE)
    var numAverage: Long? = 0,
    @get:PropertyName(Constants.ReviewKeys.ReviewStats.KEY_NUM_POOR)
    @set:PropertyName(Constants.ReviewKeys.ReviewStats.KEY_NUM_POOR)
    var numPoor: Long? = 0,
    @get:PropertyName(Constants.ReviewKeys.ReviewStats.KEY_NUM_TERRIBLE)
    @set:PropertyName(Constants.ReviewKeys.ReviewStats.KEY_NUM_TERRIBLE)
    var numTerrible: Long? = 0,
)

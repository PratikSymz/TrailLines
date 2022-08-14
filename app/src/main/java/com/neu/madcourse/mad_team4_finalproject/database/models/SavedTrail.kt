package com.neu.madcourse.mad_team4_finalproject.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.neu.madcourse.mad_team4_finalproject.models_nps.Activity
import java.io.Serializable

/**
 * This class will create the database table called as saved_trails
 */
@Entity(tableName = "saved_trails_table")
data class SavedTrail(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val trailID: String,
    val imageUrl: String,  // TODO: Test list of String's
    val shortName: String,
    val fullName: String,
    val designation: String,
    val address: String,     // TODO: Check if allowed [NO]
    val numReviewers: Long,
    val totalRatings: Double,
    val description: String,
    val latitude: String,
    val longitude: String,
    val weatherInfo: String,
    val contact: String,
    val email: String,// TODO: Check if allowed [NO]
    val mapImageUrl: String,
    val activities : String
) : Serializable

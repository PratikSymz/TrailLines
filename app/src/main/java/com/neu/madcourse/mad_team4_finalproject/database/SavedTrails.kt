package com.neu.madcourse.mad_team4_finalproject.database

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This class will create the database table called as saved_trails
 */
@Entity(tableName = "saved_trails")
data class SavedTrails(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val hikeImageUrl: Uri,
    val shortName: String,
    val longName: String,
    val location: String,
    val difficultyLevel: String,
    val numOfReviews: Long,
    val ratings: Double,
    val trailDescription: String,
    val trailLength: Double,
    val trailElevation: Double,
    val mapImageUrl: Uri
)

package com.neu.madcourse.mad_team4_finalproject.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * This class will create the database table called as saved_trails
 */

@Parcelize
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
):Parcelable

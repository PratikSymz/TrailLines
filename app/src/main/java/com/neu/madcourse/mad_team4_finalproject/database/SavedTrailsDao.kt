package com.neu.madcourse.mad_team4_finalproject.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query

/**
 * This class contains all methods for accessing the savedTrail Database
 */
@Dao
interface SavedTrailsDao {
    @Insert(onConflict = IGNORE)
    suspend fun addTrails(savedTrails: SavedTrails)


    @Query("SELECT * FROM saved_trails ORDER BY id ASC")
    fun readAllData(): LiveData<List<SavedTrails>>


}
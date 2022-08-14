package com.neu.madcourse.mad_team4_finalproject.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.neu.madcourse.mad_team4_finalproject.models.SavedTrails

/**
 * This class contains all methods for accessing the savedTrail Database
 */
@Dao
interface SavedTrailsDao {
    @Insert(onConflict = IGNORE)
    suspend fun addTrails(savedTrails: SavedTrails)

    @Update
    suspend fun updateTrails(savedTrail: SavedTrails)

    @Delete
    suspend fun deleteTrail(savedTrail: SavedTrails)

    @Query("DELETE FROM saved_trails")
    suspend fun deleteAllTrails()


    @Query("SELECT * FROM saved_trails ORDER BY id ASC")
    fun readAllData(): LiveData<List<SavedTrails>>


}
package com.neu.madcourse.mad_team4_finalproject.database;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.neu.madcourse.mad_team4_finalproject.database.models.SavedTrail;

import java.util.List;

@androidx.room.Dao
public interface SavedTrailsDAO {
    // Verify existing record in database
    @Query("SELECT EXISTS(SELECT * FROM saved_trails_table WHERE trailID = :trailID)")
    LiveData<Boolean> doesDataExist(String trailID);

    // Add data to database
    @Insert
    void insert(SavedTrail savedTrails);

    // Update the data in database.
    @Update
    void update(SavedTrail savedTrails);

    // Delete data from database
    @Delete
    void delete(SavedTrail savedTrails);

    // Delete data by Trail ID from database
    @Query("DELETE FROM saved_trails_table WHERE trailID = :trailID")
    void deleteByID(String trailID);

    // Query to delete all trails from database
    @Query("DELETE FROM saved_trails_table")
    void deleteAllData();

    // Query to read all the trails from database.
    // Ordering in ascending order with row id.
    @Query("SELECT * FROM saved_trails_table ORDER BY id ASC")
    LiveData<List<SavedTrail>> readAllData();
}

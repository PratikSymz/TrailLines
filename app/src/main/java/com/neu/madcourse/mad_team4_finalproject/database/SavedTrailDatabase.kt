package com.neu.madcourse.mad_team4_finalproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** this
 * this class contains the database holder and serves as the main
 * access point for the underlying connection to our app's data
 */

@Database(entities = [SavedTrails::class], version = 1, exportSchema = false)
abstract class SavedTrailDatabase : RoomDatabase() {

    abstract fun savedTrailsDao(): SavedTrailsDao

    /**
     * Creating a Companion object
     * This helps to make all the objects visible to other classes in the database package
     */
    companion object {
        // using volatile label to make the rights visible other threads
        @Volatile
        private var INSTANCE: SavedTrailDatabase? = null

        fun getTrailDatabase(context: Context): SavedTrailDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SavedTrailDatabase::class.java,
                    "saved_trail_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }


    }

}
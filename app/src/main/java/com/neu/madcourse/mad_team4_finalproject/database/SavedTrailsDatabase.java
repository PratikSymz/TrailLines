package com.neu.madcourse.mad_team4_finalproject.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.neu.madcourse.mad_team4_finalproject.database.models.SavedTrail;

@Database(entities = {SavedTrail.class}, version = 2)
public abstract class SavedTrailsDatabase extends RoomDatabase {
    /* The Database class instance */
    private static SavedTrailsDatabase INSTANCE;

    /* The DAO abstract variable */
    public abstract SavedTrailsDAO DAO();

    /* Create a callback for the room database */
    private static final RoomDatabase.Callback mDBCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase database) {
            super.onCreate(database);
            // Populate data
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    /* Extract the database instance */
    public static synchronized SavedTrailsDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            // Create a new instance
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SavedTrailsDatabase.class,
                            "saved_trails_database")
                    // add fall back to destructive migration to the database.
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    // add callback to the database.
                    .addCallback(mDBCallback)
                    .build();
        }

        return INSTANCE;
    }

    /* Async task class to perform database task on background thread */
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDbAsyncTask(SavedTrailsDatabase instance) {
            SavedTrailsDAO daoInterface = instance.DAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}

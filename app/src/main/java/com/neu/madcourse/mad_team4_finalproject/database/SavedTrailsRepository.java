package com.neu.madcourse.mad_team4_finalproject.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.neu.madcourse.mad_team4_finalproject.database.models.SavedTrail;

import java.util.List;

public class SavedTrailsRepository {
    /* The DAO interface instance */
    private final SavedTrailsDAO mInterface;

    /* The instance of all Saved trails */
    private final LiveData<List<SavedTrail>> mSavedTrails;

    /* Constructor */
    public SavedTrailsRepository(Application application) {
        SavedTrailsDatabase database = SavedTrailsDatabase.getInstance(application);
        // Instantiate the interface instance
        mInterface = database.DAO();
        // Execute the saved list trail list query
        mSavedTrails = mInterface.readAllData();
    }

    /* Method to verify the existence of data in the database */
    public LiveData<Boolean> doesDataExist(String trailID) {
        return mInterface.doesDataExist(trailID);
    }

    /* Method to insert data to the database */
    public void insert(SavedTrail model) {
        new InsertTrailAsyncTask(mInterface).execute(model);
    }

    /* Method to update data in the database */
    public void update(SavedTrail model) {
        new UpdateTrailAsyncTask(mInterface).execute(model);
    }

    /* Method to delete data in the database */
    public void delete(SavedTrail model) {
        new DeleteTrailAsyncTask(mInterface).execute(model);
    }

    /* Method to delete data by Trail ID in the database */
    public void deleteByID(String trailID) {
        new DeleteIDAsyncTask(mInterface).execute(trailID);
    }

    /* Method to delete all data in the database */
    public void deleteAllData() {
        new DeleteAllTrailsAsyncTask(mInterface).execute();
    }

    /* Method to read all data from the database */
    public LiveData<List<SavedTrail>> readAllData() {
        return mSavedTrails;
    }

    /**
     * The Async tasks for all the database actions
     */
    /* Async task method to insert a new trail to DB */
    private static class InsertTrailAsyncTask extends AsyncTask<SavedTrail, Void, Void> {
        private final SavedTrailsDAO mInterface;

        private InsertTrailAsyncTask(SavedTrailsDAO daoInterface) {
            mInterface = daoInterface;
        }

        @Override
        protected Void doInBackground(SavedTrail... model) {
            // Insert our model in DAO.
            mInterface.insert(model[0]);
            return null;
        }
    }

    /* Async task method to update a trail in DB */
    private static class UpdateTrailAsyncTask extends AsyncTask<SavedTrail, Void, Void> {
        private final SavedTrailsDAO mInterface;

        private UpdateTrailAsyncTask(SavedTrailsDAO daoInterface) {
            mInterface = daoInterface;
        }

        @Override
        protected Void doInBackground(SavedTrail... models) {
            // Update model in DAO.
            mInterface.update(models[0]);
            return null;
        }
    }

    /* Async task method to delete a trail from DB */
    private static class DeleteTrailAsyncTask extends AsyncTask<SavedTrail, Void, Void> {
        private final SavedTrailsDAO mInterface;

        private DeleteTrailAsyncTask(SavedTrailsDAO daoInterface) {
            mInterface = daoInterface;
        }

        @Override
        protected Void doInBackground(SavedTrail... models) {
            // Delete model from DAO
            mInterface.delete(models[0]);
            return null;
        }
    }

    /* Async task method to delete a trail by ID from DB */
    private static class DeleteIDAsyncTask extends AsyncTask<String, Void, Void> {
        private final SavedTrailsDAO mInterface;

        private DeleteIDAsyncTask(SavedTrailsDAO daoInterface) {
            mInterface = daoInterface;
        }

        @Override
        protected Void doInBackground(String... trailIDs) {
            // Delete model from DAO
            mInterface.deleteByID(trailIDs[0]);
            return null;
        }
    }

    /* Async task method to delete all saved trails from DB */
    private static class DeleteAllTrailsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final SavedTrailsDAO mInterface;

        private DeleteAllTrailsAsyncTask(SavedTrailsDAO daoInterface) {
            mInterface = daoInterface;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Delete all models from DAO
            mInterface.deleteAllData();
            return null;
        }
    }
}

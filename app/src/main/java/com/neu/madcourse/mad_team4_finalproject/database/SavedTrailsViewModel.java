package com.neu.madcourse.mad_team4_finalproject.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.neu.madcourse.mad_team4_finalproject.database.models.SavedTrail;

import java.util.List;

public class SavedTrailsViewModel extends AndroidViewModel {
    /* The Saved trails repository instance */
    private final SavedTrailsRepository mRepository;

    /*The Saved trails list reference */
    private final LiveData<List<SavedTrail>> mSavedTrails;

    public SavedTrailsViewModel(@NonNull Application application) {
        super(application);

        // Instantiate the repository
        mRepository = new SavedTrailsRepository(application);
        // Execute the get all saved trails query
        mSavedTrails = mRepository.readAllData();
    }

    /* Method to insert data to the repository */
    public void saveTrail(SavedTrail model) {
        mRepository.insert(model);
    }

    /* Method to update data in the repository */
    public void updateTrail(SavedTrail model) {
        mRepository.update(model);
    }

    /* Method to delete data from the repository */
    public void deleteTrail(SavedTrail model) {
        mRepository.delete(model);
    }

    /* Method to delete data by ID from the repository */
    public void deleteTrailByID(String trailID) {
        mRepository.deleteByID(trailID);
    }

    /* Method to delete all data from the repository */
    public void deleteAllTrails() {
        mRepository.deleteAllData();
    }

    /* Method to read all saved data from the repository */
    public LiveData<List<SavedTrail>> getSavedTrails() {
        return mSavedTrails;
    }

    /* Method to verify the existence of a trail in the repository */
    public LiveData<Boolean> isTrailSaved(String trailID) {
        return mRepository.doesDataExist(trailID);
    }
}

package com.neu.madcourse.mad_team4_finalproject.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.neu.madcourse.mad_team4_finalproject.models.SavedTrails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * provide data to the UI and survive config changes
 * this class will communicate with Repo class and the app UI
 */
class SavedTrailViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<SavedTrails>>
    private val repository: SavedTrailRepository

    init {
        val savedTrailsDao = SavedTrailDatabase.getTrailDatabase(application).savedTrailsDao()
        repository = SavedTrailRepository(savedTrailsDao)
        readAllData = repository.readAllData
    }

    fun addTrails(savedTrails: SavedTrails) {
        // this will run in the background thread
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTrails(savedTrails)
        }
    }


    /**
     * Methods to delete single trails as well all trails
     */
    fun deleteTrail(savedTrail:SavedTrails){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTrail(savedTrail)
        }
    }

    fun deleteAllTrails(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTrails()
        }
    }
}
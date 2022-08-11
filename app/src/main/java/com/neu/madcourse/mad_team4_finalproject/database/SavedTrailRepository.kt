package com.neu.madcourse.mad_team4_finalproject.database

import androidx.lifecycle.LiveData
import com.neu.madcourse.mad_team4_finalproject.models.SavedTrails


/**
 * This repo class will abstract access to multiple data sources
 * not part of the architecture components library
 * good practice to separate code and architecture
 */
class SavedTrailRepository (private val savedTrailsDao: SavedTrailsDao){

    val readAllData: LiveData<List<SavedTrails>> = savedTrailsDao.readAllData()

    suspend fun addTrails(savedTrails: SavedTrails){
        savedTrailsDao.addTrails(savedTrails)
    }
    suspend fun deleteTrail(savedTrail: SavedTrails){
        savedTrailsDao.deleteTrail(savedTrail)
    }
    suspend fun deleteAllTrails(){
        savedTrailsDao.deleteAllTrails()
    }
    suspend fun updateTrails(savedTrail: SavedTrails){
        savedTrailsDao.updateTrails(savedTrail)
    }
}
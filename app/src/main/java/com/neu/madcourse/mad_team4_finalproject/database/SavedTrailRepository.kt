package com.neu.madcourse.mad_team4_finalproject.database

import androidx.lifecycle.LiveData


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
}
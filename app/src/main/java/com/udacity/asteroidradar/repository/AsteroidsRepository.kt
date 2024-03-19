package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import asDomainModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.network.NasaApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidsDatabase) {
    val asteroids: LiveData<List<Asteroid>> = database.asteroidDao.getAsteroids().map {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids(apiKey: String, startDate: String, endDate: String) {
        withContext(Dispatchers.IO) {
            //fetch from api
            val asteroidsData = NasaApi.service.getAsteroids(apiKey, startDate, endDate)
            val asteroidsJson = JSONObject(asteroidsData)
            val asteroidsEntities = parseAsteroidsJsonResult(asteroidsJson)

            //insert entities into db
            database.asteroidDao.insertList(asteroidsEntities)
        }
    }
}
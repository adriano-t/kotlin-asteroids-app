package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.NasaApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception

class AsteroidsRepository(private val database: AsteroidsDatabase) {
    val asteroids: LiveData<List<Asteroid>> = database.asteroidDao.getAsteroids().map {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids(startDate: String, endDate: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = NasaApi.service.getAsteroids(Constants.API_KEY, startDate, endDate)
                val asteroidsData = response.string()
                val asteroidsJson = JSONObject(asteroidsData)
                //fetch from Nasa api
                val asteroidsEntities = parseAsteroidsJsonResult(asteroidsJson)
                //insert entities into db
                database.asteroidDao.insertList(asteroidsEntities)
            } catch (e: Exception) {
                println("Error retrieving asteroids data")
                e.printStackTrace()
                print(e.message)
            }

        }
    }
}
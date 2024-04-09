package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainViewModel(private val application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = AsteroidsRepository(database)

    val asteroids = repository.asteroids
    val pictureOfDay = repository.pictureOfDay


    init {
        viewModelScope.launch {
            val currentDate = Calendar.getInstance().time
            val endDate = Calendar.getInstance()
            endDate.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            val startDate = dateFormat.format(currentDate)
            val formattedEndDate = dateFormat.format(endDate.time)
            repository.refreshAsteroids(startDate, formattedEndDate)

            repository.refreshPictureOfDay()

        }
    }


    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}
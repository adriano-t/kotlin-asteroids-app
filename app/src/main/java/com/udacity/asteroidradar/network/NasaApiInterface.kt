package com.udacity.asteroidradar.network

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.http.Query
import retrofit2.http.GET

interface NasaApiInterface {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("api_key") apiKey: String,
        @Query("start_date") start: String,
        @Query("end_date") end: String
    ): ResponseBody

}
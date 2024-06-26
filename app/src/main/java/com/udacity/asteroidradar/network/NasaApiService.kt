package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object NasaApi {
    private val converterFactory: MoshiConverterFactory = MoshiConverterFactory.create(moshi)
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(converterFactory) //Add converter factory for serialization and deserialization of objects.
        .baseUrl(BASE_URL)
        .build()

    val service : NasaApiInterface by lazy {
        //Create an implementation of the API endpoints defined by the service interface.
        retrofit.create(NasaApiInterface::class.java)
    }
}

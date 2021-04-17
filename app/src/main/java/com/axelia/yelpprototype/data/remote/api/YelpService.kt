package com.axelia.yelpprototype.data.remote.api

import com.axelia.yelpprototype.model.Businesses
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YelpService {

    @GET("businesses/search")
    suspend fun getBusinesses(
        @Query("term") term: String?,
        @Query("location") location: String?
    ): Response<Businesses>

    @GET("businesses/search")
    suspend fun getBusinessesByCurrentLocation(
        @Query("term") term: String?,
        @Query("latitude") latiude: Double,
        @Query("longitude") longitude: Double
    ): Response<Businesses>

    companion object {
        const val API_URL = "https://api.yelp.com/v3/"
    }
}
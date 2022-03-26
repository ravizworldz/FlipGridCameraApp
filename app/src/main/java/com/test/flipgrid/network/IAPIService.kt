package com.test.flipgrid.network

import com.test.flipgrid.BuildConfig
import com.test.flipgrid.models.CatBreedList
import com.test.flipgrid.utils.AppConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface IAPIService {
    //https://docs.thecatapi.com/api-reference/breeds/breeds-list

    @GET(AppConstants.BREED_LIST)
    @Headers("${AppConstants.X_API_KEY}: ${BuildConfig.API_KEY}")
    suspend fun getCatBreedsListFromAPI(): Response<CatBreedList>

    //@GET(AppConstants.BREED_LIST)
   // suspend fun getCatBreedsListFromAPI(@HeaderMap headers: Map<String, String>): Response<CatBreedList>
}
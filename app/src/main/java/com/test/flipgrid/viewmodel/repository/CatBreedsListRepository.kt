package com.test.flipgrid.viewmodel.repository

import androidx.lifecycle.MutableLiveData
import com.test.flipgrid.models.CatBreedList
import com.test.flipgrid.network.IAPIService
import com.test.flipgrid.utils.DataCallback

class CatBreedsListRepository (val apiService: IAPIService) {
    /**
     * Make api call to get the data from the server.
     * get all the cat breeds list.
     */
    suspend fun getCatBreedsList(listObserver: MutableLiveData<DataCallback<CatBreedList>>) {
       /* val header = HashMap<String, String>()
        header.put(AppConstants.X_API_KEY, BuildConfig.API_KEY)*/
        val response = apiService.getCatBreedsListFromAPI()
        if(response.isSuccessful) {
            listObserver.postValue(DataCallback.success(response.body()))
        } else {
            listObserver.postValue(DataCallback.error(response.message(), null))
        }
    }
}
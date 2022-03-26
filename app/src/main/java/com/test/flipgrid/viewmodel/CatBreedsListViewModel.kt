package com.test.flipgrid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.flipgrid.FlipGridApp
import com.test.flipgrid.models.CatBreedList
import com.test.flipgrid.utils.AppConnectivityManager
import com.test.flipgrid.utils.DataCallback
import com.test.flipgrid.viewmodel.repository.CatBreedsListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class CatBreedsListViewModel (appContext: FlipGridApp): ViewModel() {

    private var catsBreedListObserver: MutableLiveData<DataCallback<CatBreedList>>
    private var job: Job? = null

    @Inject
    lateinit var catBreedsListRepo: CatBreedsListRepository

    @Inject
    lateinit var appConnectivityManager: AppConnectivityManager

    init {
        appContext.getAppComponent().inject(this)
        catsBreedListObserver = MutableLiveData()
    }

    /**
     * Check the internet connectivity.
     */
    fun hasInternetConnection(): Boolean {
        return appConnectivityManager.isConnectedToInternet()
    }

    /**
     * return the observer to observe the results
     * need to register on activity
     */
    fun getCatBreedsListObserver(): MutableLiveData<DataCallback<CatBreedList>> {
        return catsBreedListObserver
    }

    /**
     * Make the api call through repository
     */
    fun getCatBreedsList() {
        catsBreedListObserver.postValue(DataCallback.loading(null))
        job = viewModelScope.launch(Dispatchers.IO) {
           catBreedsListRepo.getCatBreedsList(catsBreedListObserver)
        }
    }

    /**
     * Called when view model destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
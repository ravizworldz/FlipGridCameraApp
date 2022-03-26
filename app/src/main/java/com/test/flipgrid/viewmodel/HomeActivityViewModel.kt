package com.test.flipgrid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.flipgrid.FlipGridApp
import com.test.flipgrid.models.entity.UserProfileEntity
import com.test.flipgrid.viewmodel.repository.CreateProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeActivityViewModel (appContext: FlipGridApp): ViewModel() {

    private var profileObserver: MutableLiveData<List<UserProfileEntity>>

    @Inject
    lateinit var createProfileRepository: CreateProfileRepository

    init {
        appContext.getAppComponent().inject(this)
        profileObserver = MutableLiveData()
    }

    /**
     * return observer to called when delay is completed.
     * need to be observed on activity
     */
    fun getProfileObserver(): MutableLiveData<List<UserProfileEntity>> {
        return profileObserver
    }

    /**
     * Called to save data in db
     */
    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            createProfileRepository.getAllRecords( profileObserver)
        }
    }
}
package com.test.flipgrid.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.flipgrid.FlipGridApp
import com.test.flipgrid.models.entity.UserProfileEntity
import com.test.flipgrid.utils.DataCallback
import com.test.flipgrid.utils.Utils
import com.test.flipgrid.viewmodel.repository.CreateProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateProfileViewModel(appContext: FlipGridApp): ViewModel() {

    private var createProfileObserver: MutableLiveData<DataCallback<Boolean>>
    private var profileObserver: MutableLiveData<List<UserProfileEntity>>
    private var job: Job? = null

    @Inject
    lateinit var createProfileRepository: CreateProfileRepository

    init {
        appContext.getAppComponent().inject(this)
        createProfileObserver = MutableLiveData()
        profileObserver = MutableLiveData()
    }

    /**
     * return observer to called when delay is completed.
     * need to be observed on activity
     */
    fun getCreateProfileObserver(): MutableLiveData<DataCallback<Boolean>> {
        return createProfileObserver
    }

    /**
     * Return observer to register on activity to observer changes.
     */
    fun getProfileObserver(): MutableLiveData<List<UserProfileEntity>> {
        return profileObserver
    }

    /**
     * Called to save data in db
     */
    fun createProfile(userEntity: UserProfileEntity) {
        job = viewModelScope.launch(Dispatchers.IO) {
            createProfileRepository.insertRecord(userEntity, createProfileObserver)
        }
    }

    /**
     * Read data from DB and send update on UI.
     */
    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            createProfileRepository.getAllRecords(profileObserver)
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
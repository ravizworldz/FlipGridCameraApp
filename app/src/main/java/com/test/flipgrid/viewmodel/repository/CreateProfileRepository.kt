package com.test.flipgrid.viewmodel.repository

import androidx.lifecycle.MutableLiveData
import com.test.flipgrid.db.UserProfileDao
import com.test.flipgrid.models.entity.UserProfileEntity
import com.test.flipgrid.utils.DataCallback

class CreateProfileRepository(val userProfileDao: UserProfileDao) {

    /**
     * Store record in DB
     * send update to observer.
     */
    suspend fun insertRecord(userEntity: UserProfileEntity, createProfileObserver: MutableLiveData<DataCallback<Boolean>>) {
        userProfileDao.insertRecords(userEntity)
        createProfileObserver.postValue(DataCallback.success(true))
    }

    /**
     * Read record from DB
     * send update to observer
     */
    suspend fun getAllRecords(createProfileObserver: MutableLiveData<List<UserProfileEntity>>) {
        val list = userProfileDao.getAllProfile()
        createProfileObserver.postValue(list)
    }
}
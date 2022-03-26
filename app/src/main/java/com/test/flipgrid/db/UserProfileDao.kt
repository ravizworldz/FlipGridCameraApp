package com.test.flipgrid.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.flipgrid.models.entity.UserProfileEntity

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM profile")
     fun getAllProfile(): List<UserProfileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertRecords(repositoryData: UserProfileEntity)
}
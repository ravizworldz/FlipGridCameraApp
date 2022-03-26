package com.test.flipgrid.models.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
class UserProfileEntity (
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id")val id: Int = 0,
    @ColumnInfo(name = "fname")val fName: String?,
    @ColumnInfo(name = "lname")val lName: String?,
    @ColumnInfo(name = "email")val email: String?,
    @ColumnInfo(name = "password")val password: String?,
    @ColumnInfo(name = "website")val website: String?,
    @ColumnInfo(name = "image")val image: String?
)
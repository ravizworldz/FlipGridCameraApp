package com.test.flipgrid.di

import android.app.Application
import android.content.Context
import com.test.flipgrid.FlipGridApp
import com.test.flipgrid.db.AppDatabase
import com.test.flipgrid.db.UserProfileDao
import com.test.flipgrid.utils.AppConnectivityManager
import io.mockk.mockk

class TestAppModule(private val application: Application): AppModule(application as FlipGridApp) {
    override fun getApplicationContext(): Context {
        return application
    }

    override fun getConnectivityManager(): AppConnectivityManager {
        return  mockk()
    }

    override fun getRoomDbInstance(context: Context): AppDatabase {
        return mockk()
    }

    override fun getUserProfileDao(appDatabase: AppDatabase): UserProfileDao {
        return mockk()
    }
}
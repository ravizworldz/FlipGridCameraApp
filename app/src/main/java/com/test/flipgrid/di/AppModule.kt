package com.test.flipgrid.di

import android.content.Context
import com.test.flipgrid.FlipGridApp
import com.test.flipgrid.db.AppDatabase
import com.test.flipgrid.db.UserProfileDao
import com.test.flipgrid.network.APIProvider
import com.test.flipgrid.network.IAPIService
import com.test.flipgrid.utils.AppConnectivityManager
import com.test.flipgrid.viewmodel.repository.CatBreedsListRepository
import com.test.flipgrid.viewmodel.repository.CreateProfileRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
open class AppModule(private val application: FlipGridApp) {
    @Singleton
    @Provides
    fun getApiServiceInstance(retrofit: Retrofit): IAPIService {
        return retrofit.create(IAPIService::class.java)
    }

    @Provides
    @Singleton
    open fun getApplicationContext(): Context {
        return application
    }

    @Provides
    @Singleton
    open fun getConnectivityManager(): AppConnectivityManager {
        return AppConnectivityManager(getApplicationContext())
    }

    @Singleton
    @Provides
    open fun getUserProfileDao(appDatabase: AppDatabase): UserProfileDao {
        return appDatabase.getUserProfileDao()
    }

    @Singleton
    @Provides
    open fun getRoomDbInstance(context: Context): AppDatabase {
        return AppDatabase.getAppDBInstance(context)
    }

    @Singleton
    @Provides
    open fun getAPIProviderInstance(): Retrofit {
        return APIProvider.getRetroInstance()
    }

    @Singleton
    @Provides
    fun getCatBreedsListRepository(apiService: IAPIService): CatBreedsListRepository {
        return CatBreedsListRepository(apiService)
    }

    @Singleton
    @Provides
    fun getCreateProfileRepository(userProfileDao: UserProfileDao): CreateProfileRepository {
        return CreateProfileRepository(userProfileDao)
    }
}
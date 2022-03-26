package com.test.flipgrid.di

import com.test.flipgrid.viewmodel.HomeActivityViewModel
import com.test.flipgrid.viewmodel.CameraViewModel
import com.test.flipgrid.viewmodel.CatBreedsListViewModel
import com.test.flipgrid.viewmodel.CreateProfileViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface IAppComponent {
    fun inject(catBreedsListViewModel: CatBreedsListViewModel)
    fun inject(createProfileViewModel: CreateProfileViewModel)
    fun inject(homeActivityViewModel: HomeActivityViewModel)
    fun inject(cameraViewModel: CameraViewModel)
}
package com.test.flipgrid.di

import com.test.flipgrid.viewmodel.CatBreedListViewModelTest
import com.test.flipgrid.viewmodel.CatProfileListViewModelTest
import com.test.flipgrid.viewmodel.DaoTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface TestAppComponent: IAppComponent {
    fun inject(catBreedListViewModelTest: CatBreedListViewModelTest)
    fun inject(catProfileListViewModelTest: CatProfileListViewModelTest)
    fun inject(daoTest: DaoTest)
}
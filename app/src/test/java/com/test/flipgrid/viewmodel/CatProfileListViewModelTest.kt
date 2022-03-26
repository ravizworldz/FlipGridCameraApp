package com.test.flipgrid.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.flipgrid.FlipGridApp
import com.test.flipgrid.di.DaggerTestAppComponent
import com.test.flipgrid.di.TestAppModule
import com.test.flipgrid.models.entity.UserProfileEntity
import org.junit.Before
import org.junit.Rule
import org.mockito.MockitoAnnotations
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class CatProfileListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val context: FlipGridApp = mockk()

    lateinit var viewModel: CreateProfileViewModel

    val createProfileViewModelMocked: CreateProfileViewModel = mockk()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val component = DaggerTestAppComponent.builder()
            .appModule(TestAppModule(application = context)).build()

        component.inject(this)
        every { context.getAppComponent() } returns component
        viewModel = CreateProfileViewModel(context)
    }
    @Test
    fun test_get_profile() {
        val list = mutableListOf<UserProfileEntity>()
        list.add(getUserProfileEntity())
        every { createProfileViewModelMocked.getProfile() }answers {
            viewModel.getProfileObserver().value = list
        }
        createProfileViewModelMocked.getProfile()
        Assert.assertNotNull(viewModel.getProfileObserver().value)
        Assert.assertEquals( viewModel.getProfileObserver().value?.size, 1)
    }

    private fun getUserProfileEntity(): UserProfileEntity {
        return UserProfileEntity(1, "ravi", "kkk", "ravik@gmail.com", "test1234", "www.google.com",  null)
    }
}
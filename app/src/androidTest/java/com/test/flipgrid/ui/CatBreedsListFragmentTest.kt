package com.test.flipgrid.ui

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.test.flipgrid.R
import com.test.flipgrid.ui.fragment.CatBreedsListFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CatBreedsListFragmentTest {
    private lateinit var scenario: FragmentScenario<CatBreedsListFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_SynchronyApp)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun scrollRecyclerView() {
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
       Espresso.onView(ViewMatchers.withId(R.id.recyclerViewCatBreeds)).perform(
            ScrollToBottomAction()
        )
    }
}
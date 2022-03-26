package com.test.flipgrid.ui

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.test.flipgrid.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CreateProfileActivityTest {

    private lateinit var scenario: ActivityScenario<CreateProfileActivity>

    @Before
    fun setUp() {
        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun testCreateProfile() {
        onView(withId(R.id.inputFirstName)).perform(ViewActions.typeText("ravi"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.inputLastName)).perform(ViewActions.typeText("kkk"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.inputEmail)).perform(ViewActions.typeText("ravi.kumar@gmail.com"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.inputPassword)).perform(ViewActions.typeText("test1234"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.inputWebsite)).perform(ViewActions.typeText("www.google.com"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.saveButton)).perform(click())
    }
}
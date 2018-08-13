package com.corrado.offersapp


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView.ViewHolder
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun shouldOpenDetailsScreen() {
        //Select the first item in recycler view
        val recyclerView = onView(
                allOf(withId(R.id.recycler_view)))
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        //Test if favorite button is displayed
        onView(withId(R.id.favorite_button)).check(matches(isDisplayed()))
    }

    @Test
    fun favoriteTest() {
        //Select the first item in recycler view
        val recyclerView = onView(
                allOf(withId(R.id.recycler_view)))
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        //Toggle a favorite
        val toggleButton = onView(
                allOf(withId(R.id.favorite_button),
                        isDisplayed()))
        toggleButton.perform(click())

        //Exit and go back
        pressBack()
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))
    }
}

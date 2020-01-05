package com.gorillamo.ui.choosablelist

import android.os.SystemClock
import android.view.View
import android.widget.Button
import androidx.core.view.get
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.allOf

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ChoosableListTests {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule<MockActivity>(MockActivity::class.java, true, false)


    @Before
    fun setUp(){

        MockActivity.layout = com.gorillamo.ui.choosablelist.test.R.layout.layout_mock

    }

    @Test
    fun verifyItemsShow() {
        // Context of the app under test.
        activityRule.launchActivity(null)

        //sleep for a sec
        Thread.sleep(1200)

        onView(allOf(withText("ONE"))).check(matches(isDisplayed()))
        onView(allOf(withText("TEN"))).check(matches(isDisplayed()))
        onView(allOf(withText("FIFTEEN"))).check(matches(isDisplayed()))

    }

    @Test
    fun verifySnapsToCenter() {
        // Context of the app under test.
        activityRule.launchActivity(null)

        //sleep for a sec for animations to finish
        Thread.sleep(1200)

        //now find the position of the view
        val fifteen = activityRule.activity.findViewById<ChoosableList>(com.gorillamo.ui.choosablelist.test.R.id.choosable).get(2)
        var initialViewPosition = fifteen.left
        var centerPosition = 0

        //Click on it
        onView(withText("FIFTEEN")).perform(click())
        Thread.sleep(500)

        //Verify that the view has changed position
        assert(initialViewPosition != fifteen.left)
        centerPosition = fifteen.left

        onView(withText("NinetyNine")).perform(click())
        Thread.sleep(500)
        onView(withText("OneThousand")).perform(click())
        Thread.sleep(500)

        val oneThousand = activityRule.activity.findViewById<ChoosableList>(com.gorillamo.ui.choosablelist.test.R.id.choosable).get(3)
        assert(centerPosition == oneThousand.left)

    }

    @Test
    fun verifyPositionStartsCorrectly() {
        MockActivity.layout = com.gorillamo.ui.choosablelist.test.R.layout.layout_mock_pos_last

        activityRule.launchActivity(null)

        //sleep for a sec for animations to finish
        Thread.sleep(1200)

        onView(allOf(withText("NegativeFive"))).check(matches(isDisplayed()))
        onView(withText("-5")).check(matches(isDisplayed()))

    }
}

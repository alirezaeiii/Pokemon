package se.appshack.android.refactoring

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import se.appshack.android.refactoring.ui.MainActivity
import se.appshack.android.refactoring.ui.MainAdapter
import se.appshack.android.refactoring.util.EspressoIdlingResource

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    /**
     * Prepare your test fixture for this test. In this case we register an IdlingResources with
     * Espresso. IdlingResource resource is a great way to tell Espresso when your app is in an
     * idle state. This helps Espresso to synchronize your test actions, which makes tests
     * significantly more reliable.
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun shouldBeAbleToLaunchMainScreen() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldBeAbleToLoadList() {
        onView(withId(R.id.recyclerview)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldBeAbleToScrollViewAndDisplaySpecies() {
        onView(withId(R.id.recyclerview)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MainAdapter.MainViewHolder>(5, click())
        )
        onView(withId(R.id.pokemonSpecies)).check(matches(withText(containsString("Species:"))))
    }

    @Test
    fun shouldBeAbleToScrollViewAndDisplayType() {
        onView(withId(R.id.recyclerview)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MainAdapter.MainViewHolder>(5, click())
        )
        onView(withId(R.id.pokemonTypes)).check(matches(withText(containsString("Type:"))))
    }

    @Test
    fun shouldBeAbleToScrollViewAndDisplayHeight() {
        onView(withId(R.id.recyclerview)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MainAdapter.MainViewHolder>(5, click())
        )
        onView(withId(R.id.pokemonHeight)).check(matches(withText(containsString("Height:"))))
    }

    @Test
    fun shouldBeAbleToScrollViewAndDisplayWeight() {
        onView(withId(R.id.recyclerview)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MainAdapter.MainViewHolder>(5, click())
        )
        onView(withId(R.id.pokemonWeight)).check(matches(withText(containsString("Weight:"))))
    }
}

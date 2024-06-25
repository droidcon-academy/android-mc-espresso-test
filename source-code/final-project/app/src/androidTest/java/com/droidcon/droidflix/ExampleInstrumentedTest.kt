package com.droidcon.droidflix

import android.app.Activity.RESULT_OK
import android.app.Instrumentation
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.espresso.web.webdriver.DriverAtoms
import androidx.test.espresso.web.webdriver.DriverAtoms.clearElement
import androidx.test.espresso.web.webdriver.DriverAtoms.findElement
import androidx.test.espresso.web.webdriver.DriverAtoms.webClick
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidcon.droidflix.ui.DroidFlixApp
import com.droidcon.droidflix.ui.theme.DroidFlixTheme
import com.mikepenz.aboutlibraries.ui.LibsActivity
import org.hamcrest.core.AllOf.allOf
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule val composeTestRule = createComposeRule()
    // use createAndroidComposeRule<YourActivity>() if you need access to
    // an activity

    @Before
    fun startActivity() {
        composeTestRule.setContent {
            DroidFlixTheme {
                DroidFlixApp()
            }
        }
    }

    @Test
    fun searchForMovie() {
        composeTestRule.onNodeWithTag("loader").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Search for movies…").assertIsDisplayed()
        composeTestRule.onNodeWithTag("search").performTextInput("red")
        composeTestRule.onNodeWithTag("search").assert(hasText("red"))
        composeTestRule.onNodeWithTag("loader").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search for movies…").assertIsNotDisplayed()

        composeTestRule.waitUntil(5_000) {
            composeTestRule.onNodeWithTag("list").fetchSemanticsNode().children.size > 1
        }

    }

    @Test
    fun checkLibraries() {
        val expectedIntent = allOf(
            hasComponent(LibsActivity::class.java.name)
        )
        val result = Instrumentation.ActivityResult(RESULT_OK, null)
        Intents.init()
        intending(expectedIntent).respondWith(result)
        composeTestRule.onNodeWithTag("options").performClick()
        composeTestRule.onNodeWithTag("libraries").performClick()
        intended(expectedIntent)
        Intents.release()
    }

    @Test
    fun checkApiDocumentation() {
        composeTestRule.onNodeWithTag("options").performClick()
        composeTestRule.onNodeWithTag("api").performClick()

        onWebView()
            .withElement(findElement(Locator.ID, "t"))
            .perform(clearElement())
            .perform(DriverAtoms.webKeys("red"))

        onWebView()
            .withElement(findElement(Locator.ID, "search-by-title-button"))
            .perform(webClick())

        onWebView()
            .withElement(findElement(Locator.CSS_SELECTOR, ".alert-success"))
    }
}
package com.droidcon.droidflix

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidcon.droidflix.ui.DroidFlixApp
import com.droidcon.droidflix.ui.theme.DroidFlixTheme

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DroidflixInstrumentedTests {

    @get:Rule
    val composeTestRule = createComposeRule()
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
    }
}
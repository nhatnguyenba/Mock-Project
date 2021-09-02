package com.example.test

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented hn21_cpl_android_03g2, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under hn21_cpl_android_03g2.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.hn21_cpl_android_03g2", appContext.packageName)
    }
}
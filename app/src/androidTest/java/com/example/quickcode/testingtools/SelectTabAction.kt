package com.example.quickcode.testingtools

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.tabs.TabLayout
import org.hamcrest.Matcher
import org.hamcrest.Matchers

class SelectTabAction(private val tabIndex: Int) : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return Matchers.allOf(ViewMatchers.isDisplayed(), ViewMatchers.isAssignableFrom(TabLayout::class.java))
    }

    override fun getDescription(): String {
        return "Select tab at index $tabIndex"
    }

    override fun perform(uiController: UiController, view: View) {
        val tabLayout = view as TabLayout
        tabLayout.getTabAt(tabIndex)?.select()
    }
}
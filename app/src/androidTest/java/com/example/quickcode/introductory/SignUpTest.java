package com.example.quickcode.introductory;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.quickcode.testingtools.CustomMatchers.withIndex;
import static com.example.quickcode.testingtools.CustomMatchers.withTextColor;

import android.view.View;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.quickcode.R;
import com.example.quickcode.introductory.onBoarding.OnBoardingActivity;
import com.example.quickcode.testingtools.MatcherUtilsJava;
import com.example.quickcode.testingtools.SelectTabAction;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SignUpTest {

    @Rule
    public ActivityScenarioRule<OnBoardingActivity> mActivityScenarioRule = new ActivityScenarioRule<>(OnBoardingActivity.class);

    @Test
    public void shouldSetGreenColorWhenOneOfTheCircleCheckIsCorrect() {
        onView(withIndex(withId(R.id.skipButton), 0))
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());

        onView(withId(R.id.tabLayout))
                .check(matches(isDisplayed()))
                .perform(new SelectTabAction(1));

        Matcher<View> hasExactTextColor = withTextColor(
                MatcherUtilsJava.hasExactTextColor(
                        R.color.darkerGreen
                )
        );

        // checkLowercase is correct
        typeTextInputEditText("a");
        onView(withId(R.id.checkLowercase))
                .check(matches(isDisplayed()))
                .check(matches(hasExactTextColor));

        // checkUpperCase is correct
        typeTextInputEditText("A");
        onView(withId(R.id.checkUpperCase))
                .check(matches(isDisplayed()))
                .check(matches(hasExactTextColor));

        // checkSpecialChar is correct
        typeTextInputEditText("!");
        onView(withId(R.id.checkSpecialChar))
                .check(matches(isDisplayed()))
                .check(matches(hasExactTextColor));

        // checkDigit is correct
        typeTextInputEditText("1");
        onView(withId(R.id.checkDigit))
                .check(matches(isDisplayed()))
                .check(matches(hasExactTextColor));

        // checkCharacterLength is correct
        typeTextInputEditText("123456");
        onView(withId(R.id.checkCharacterLength))
                .check(matches(isDisplayed()))
                .check(matches(hasExactTextColor));
    }

    @Test
    public void shouldSetAllGreenColorWhenAllOfTheCircleCheckIsCorrect() {
        onView(withIndex(withId(R.id.skipButton), 0))
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());

        onView(withId(R.id.tabLayout))
                .check(matches(isDisplayed()))
                .perform(new SelectTabAction(1));

        Matcher<View> hasExactTextColor = withTextColor(
                MatcherUtilsJava.hasExactTextColor(
                        R.color.darkerGreen
                )
        );

        // checkLowercase is correct
        // checkUpperCase is correct
        // checkSpecialChar is correct
        // checkDigit is correct
        // checkCharacterLength is correct

        typeTextInputEditText("123!@#qweQWE");

        onView(withId(R.id.checkLowercase))
                .check(matches(isDisplayed()))
                .check(matches(hasExactTextColor));

        onView(withId(R.id.checkUpperCase))
                .check(matches(isDisplayed()))
                .check(matches(hasExactTextColor));

        onView(withId(R.id.checkSpecialChar))
                .check(matches(isDisplayed()))
                .check(matches(hasExactTextColor));

        onView(withId(R.id.checkDigit))
                .check(matches(isDisplayed()))
                .check(matches(hasExactTextColor));

        onView(withId(R.id.checkCharacterLength))
                .check(matches(isDisplayed()))
                .check(matches(hasExactTextColor));
    }

    private void typeTextInputEditText(String stringToBeTyped) {
        onView(withId(R.id.password))
                .check(matches(isDisplayed()))
                .perform(
                        ViewActions.clearText(),
                        ViewActions.typeText(stringToBeTyped),
                        ViewActions.closeSoftKeyboard()
                );
    }
}
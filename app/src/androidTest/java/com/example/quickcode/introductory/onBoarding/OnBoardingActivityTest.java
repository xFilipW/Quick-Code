package com.example.quickcode.introductory.onBoarding;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.example.quickcode.testingtools.CustomMatchers.withIndex;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.quickcode.R;
import com.example.quickcode.loginRegister.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class OnBoardingActivityTest {

    @Rule
    public IntentsRule intentsRule = new IntentsRule();

    @Rule
    public ActivityScenarioRule<OnBoardingActivity> activityScenarioRule = new ActivityScenarioRule<>(OnBoardingActivity.class);

    @Test
    public void testSkipButton_shouldOpenActivity() {
        onView(withIndex(withId(R.id.skipButton), 0))
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());

        intended(hasComponent(LoginActivity.class.getName()));
    }
}
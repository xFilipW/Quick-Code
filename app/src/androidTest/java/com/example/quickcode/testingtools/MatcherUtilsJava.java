package com.example.quickcode.testingtools;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.ref.WeakReference;

public class MatcherUtilsJava {

    public static Matcher<String> hasExactString(final String expected) {

        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(String charSequence) {
                return expected.equals(charSequence);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has exact string \"" + expected + "\"");
            }
        };
    }

    public static Matcher<TextView> hasExactTextColor(@ColorRes final int colorResId) {
        return new BoundedMatcher<>(TextView.class) {

            private WeakReference<TextView> textViewWeakReference;

            @Override
            protected boolean matchesSafely(TextView view) {
                ColorStateList expectedColor = ColorStateList.valueOf(ContextCompat.getColor(view.getContext(), colorResId));
                ColorStateList actualColor = view.getTextColors();

                textViewWeakReference = new WeakReference<>(view);

                return expectedColor == actualColor;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with color: ");

                if (textViewWeakReference != null) {
                    TextView textView = textViewWeakReference.get();
                    if (textView != null) {
                        description.appendValue(ContextCompat.getColor(textView.getContext(), colorResId));
                    }
                }
            }
        };
    }

}

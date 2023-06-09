package com.example.quickcode.testingtools;

import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class CustomMatchers {

    public static Matcher<View> withTextColor(Matcher<TextView> textViewMatcher) {
        return new WithTextColorMatcher(textViewMatcher);
    }

    public static Matcher<View> withIndex(Matcher<View> matcher, int index) {
        return new TypeSafeMatcher<>() {

            private int currentIndex = 0;
            private int viewObjHash = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText(String.format("with index: %d ", index));
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (matcher.matches(view) && currentIndex++ == index) {
                    viewObjHash = view.hashCode();
                }
                return view.hashCode() == viewObjHash;
            }
        };
    }

    private static class WithTextColorMatcher implements Matcher<View> {

        private final Matcher<TextView> textViewMatcher;

        public WithTextColorMatcher(Matcher<TextView> textColor) {
            this.textViewMatcher = textColor;
        }

        @Override
        public boolean matches(Object item) {
            return textViewMatcher.matches(item);
        }

        @Override
        public void describeMismatch(Object item, Description mismatchDescription) {
            textViewMatcher.describeMismatch(item, mismatchDescription);
        }

        @Override
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
            //...
        }

        @Override
        public void describeTo(Description description) {
            textViewMatcher.describeTo(description);
        }
    }
}

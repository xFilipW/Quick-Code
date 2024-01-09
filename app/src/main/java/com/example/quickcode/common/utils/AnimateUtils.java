package com.example.quickcode.common.utils;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AnimateUtils {

    public static void animateStatusBarColor(AppCompatActivity activity, @ColorRes int startColor, @ColorRes int endColor) {
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();

        int animationDuration = 500;
        int startColorVal = ContextCompat.getColor(activity, startColor);
        int endColorVal = ContextCompat.getColor(activity, endColor);

        ValueAnimator colorAnimator = ValueAnimator.ofObject(argbEvaluator, startColorVal, endColorVal);
        colorAnimator.setDuration(animationDuration);
        colorAnimator.addUpdateListener(animator -> {
            int animatedColor = (int) animator.getAnimatedValue();
            activity.getWindow().setStatusBarColor(animatedColor);
        });

        colorAnimator.start();
    }

    public static void smoothResizeView(View view,
                                        int currentHeight,
                                        int newHeight) {

        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(currentHeight, newHeight)
                .setDuration(500);

        slideAnimator.addUpdateListener(animation1 -> {
            Integer value = (Integer) animation1.getAnimatedValue();
            view.getLayoutParams().height = value.intValue();
            view.requestLayout();
        });

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.play(slideAnimator);
        animationSet.start();
    }
}

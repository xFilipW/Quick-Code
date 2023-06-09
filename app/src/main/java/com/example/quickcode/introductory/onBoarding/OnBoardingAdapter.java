package com.example.quickcode.introductory.onBoarding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingAdapter extends FragmentPagerAdapter {

    private final List<Fragment> list = new ArrayList<>();

    public OnBoardingAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        list.add(new OnBoardingFragment1());
        list.add(new OnBoardingFragment2());
        list.add(new OnBoardingFragment3());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

}

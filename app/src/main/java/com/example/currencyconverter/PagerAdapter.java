package com.example.currencyconverter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        mNumOfTabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return ConverterTabFragment.newInstance();
            case 1: return ListTabFragment.newInstance();
            default: return new ConverterTabFragment();
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}


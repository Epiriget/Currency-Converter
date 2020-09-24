package com.example.currencyconverter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.currencyconverter.ConverterTabFragment;
import com.example.currencyconverter.ListTabFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private Converter mConverter;
    public PagerAdapter(@NonNull FragmentManager fm, int behavior, Converter converter) {
        super(fm, behavior);
        mNumOfTabs = behavior;
        mConverter = converter;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return ConverterTabFragment.newInstance(mConverter);
            case 1: return ListTabFragment.newInstance("par1", "par2");
            default: return new ConverterTabFragment(mConverter
            );
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}


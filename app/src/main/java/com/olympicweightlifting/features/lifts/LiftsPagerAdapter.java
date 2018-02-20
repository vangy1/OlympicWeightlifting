package com.olympicweightlifting.features.lifts;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class LiftsPagerAdapter extends FragmentStatePagerAdapter {
    public static final String BUNDLE_LIFTS_FRAGMENT_DATA = "BUNDLE_LIFTS_FRAGMENT_DATA";
    public static final String BUNDLE_LIFTS_VIDEO_URL = "BUNDLE_LIFTS_VIDEO_URL";

    private List<Fragment> liftsFragmentList = new ArrayList<>();

    public static class LiftsPagerAdapterBuilder {
        List<Fragment> liftsFragmentList = new ArrayList<>();

        FragmentManager fragmentManager;

        LiftsPagerAdapterBuilder(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
        }

        LiftsPagerAdapterBuilder addFragment(LiftsFragmentData fragmentData) {
            Bundle fragmentArguments = new Bundle();
            fragmentArguments.putString(BUNDLE_LIFTS_FRAGMENT_DATA, new Gson().toJson(fragmentData.getLiftsContentDataList()));
            fragmentArguments.putString(BUNDLE_LIFTS_VIDEO_URL, fragmentData.getFloatingButtonVideoUrl());
            this.liftsFragmentList.add(LiftsFragment.newInstance(fragmentArguments));
            return this;
        }

        public LiftsPagerAdapter build() {
            return new LiftsPagerAdapter(fragmentManager, liftsFragmentList);
        }
    }

    public LiftsPagerAdapter(FragmentManager fragmentManager,
                             List<Fragment> liftsFragmentList) {
        super(fragmentManager);
        this.liftsFragmentList = liftsFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return liftsFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return liftsFragmentList.size();
    }
}

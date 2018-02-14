package com.olympicweightlifting.features.lifts;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.google.gson.Gson;
import com.olympicweightlifting.R;

import java.util.ArrayList;
import java.util.List;

public class LiftsPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> liftsFragmentList = new ArrayList<>();

    public static class LiftsPagerAdapterBuilder {
        List<Fragment> liftsFragmentList = new ArrayList<>();

        FragmentManager fragmentManager;
        Context context;

        LiftsPagerAdapterBuilder(FragmentManager fragmentManager, Context context) {
            this.fragmentManager = fragmentManager;
            this.context = context;
        }

        LiftsPagerAdapterBuilder addFragment(LiftsFragmentData fragmentData) {
            Bundle fragmentArguments = new Bundle();
            fragmentArguments.putString(context.getString(R.string.lifts_content_data_list), new Gson().toJson(fragmentData.getLiftsContentDataList()));
            fragmentArguments.putString(context.getString(R.string.floating_button_video_url), fragmentData.getFloatingButtonVideoUrl());
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

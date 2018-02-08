package com.olympicweightlifting.features.records;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.olympicweightlifting.features.records.personal.RecordsPersonalFragment;
import com.olympicweightlifting.features.records.world.RecordsWorldFragment;

public class RecordsPagerAdapter extends FragmentStatePagerAdapter {
    private RecordsPersonalFragment recordsPersonalFragment = new RecordsPersonalFragment();
    private RecordsWorldFragment recordsWorldFragment = new RecordsWorldFragment();

    public RecordsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return recordsPersonalFragment;
            case 1:
                return recordsWorldFragment;
            default:
                return recordsPersonalFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}

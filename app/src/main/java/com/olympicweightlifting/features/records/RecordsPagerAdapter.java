package com.olympicweightlifting.features.records;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.olympicweightlifting.authentication.SignInFragment;
import com.olympicweightlifting.features.records.personal.RecordsPersonalFragment;
import com.olympicweightlifting.features.records.world.RecordsWorldFragment;

public class RecordsPagerAdapter extends FragmentStatePagerAdapter {
    private Fragment recordsPersonalFragment = FirebaseAuth.getInstance().getCurrentUser() != null ? new RecordsPersonalFragment() : new SignInFragment();
    private Fragment recordsWorldFragment = new RecordsWorldFragment();

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

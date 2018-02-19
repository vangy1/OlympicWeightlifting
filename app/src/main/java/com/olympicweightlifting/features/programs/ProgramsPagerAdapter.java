package com.olympicweightlifting.features.programs;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.olympicweightlifting.authentication.SignInFragment;
import com.olympicweightlifting.features.programs.create.ProgramCreateFragment;
import com.olympicweightlifting.features.programs.list.ProgramsListFragment;


public class ProgramsPagerAdapter extends FragmentStatePagerAdapter {
    private Fragment list = FirebaseAuth.getInstance().getCurrentUser() != null ? new ProgramsListFragment() : new SignInFragment();
    private Fragment create = FirebaseAuth.getInstance().getCurrentUser() != null ? new ProgramCreateFragment() : new SignInFragment();

    public ProgramsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return list;
            case 1:
                return create;
            default:
                return list;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}

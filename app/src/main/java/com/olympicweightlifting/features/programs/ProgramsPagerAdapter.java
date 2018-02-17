package com.olympicweightlifting.features.programs;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.olympicweightlifting.authentication.SignInFragment;
import com.olympicweightlifting.features.programs.create.CreateProgramFragment;
import com.olympicweightlifting.features.programs.list.ListProgramsFragment;

/**
 * Created by vangor on 12/02/2018.
 */

public class ProgramsPagerAdapter extends FragmentStatePagerAdapter {
    private Fragment list = new ListProgramsFragment();
    private Fragment create = FirebaseAuth.getInstance().getCurrentUser() != null ? new CreateProgramFragment() : new SignInFragment();

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

package com.olympicweightlifting.features.tracking;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.olympicweightlifting.authentication.SignInFragment;
import com.olympicweightlifting.features.tracking.history.TrackingHistoryFragment;
import com.olympicweightlifting.features.tracking.tracknew.TrackingNewFragment;

/**
 * Created by vangor on 12/02/2018.
 */

public class TrackingPagerAdapter extends FragmentStatePagerAdapter {
    private Fragment trackingNew = FirebaseAuth.getInstance().getCurrentUser() != null ? new TrackingNewFragment() : new SignInFragment();
    private Fragment trackingHistory = FirebaseAuth.getInstance().getCurrentUser() != null ? new TrackingHistoryFragment() : new SignInFragment();

    public TrackingPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return trackingNew;
            case 1:
                return trackingHistory;
            default:
                return trackingNew;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}

package com.olympicweightlifting.features.tracking;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;

import com.olympicweightlifting.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class TrackingActivity extends DaggerAppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.fragment_container_workouts)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        ButterKnife.bind(this);

        setupTabLayout();
        setupViewPager();
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Track new"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }

    private void setupViewPager() {
        viewPager.setAdapter(new TrackingPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        int fragmentIndex = getIntent().getIntExtra(getString(R.string.extra_fragment_index), 0);
        viewPager.setCurrentItem(fragmentIndex);
    }
}

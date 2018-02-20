package com.olympicweightlifting.features.calculators;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.olympicweightlifting.R;
import com.olympicweightlifting.mainpage.FeaturesRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class CalculatorsActivity extends DaggerAppCompatActivity {
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        ButterKnife.bind(this);

        setupTabLayout();
        setupViewPager();
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.calculators_tab_sinclair));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.calculators_tab_repmax));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.calculators_tab_loading));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }

    private void setupViewPager() {
        viewPager.setAdapter(new CalculatorsPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        int fragmentIndex = getIntent().getIntExtra(FeaturesRecyclerViewAdapter.BUNDLE_FRAGMENT_INDEX, 0);
        viewPager.setCurrentItem(fragmentIndex);
    }
}

package com.olympicweightlifting.features.records;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.olympicweightlifting.R;
import com.olympicweightlifting.mainpage.FeaturesRecyclerViewAdapter;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class RecordsActivity extends DaggerAppCompatActivity {
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Inject
    @Named("settings")
    SharedPreferences settingsSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (settingsSharedPreferences.getBoolean(getString(R.string.all_dark_theme), false)) {
            setTheme(R.style.AppTheme_Dark);
        }
        setContentView(R.layout.activity_records);
        ButterKnife.bind(this);

        setupTabLayout();
        setupViewPager();
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.records_tab_personal));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.records_tab_world));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }

    private void setupViewPager() {
        viewPager.setAdapter(new RecordsPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        int fragmentIndex = getIntent().getIntExtra(FeaturesRecyclerViewAdapter.BUNDLE_FRAGMENT_INDEX, 0);
        viewPager.setCurrentItem(fragmentIndex);
    }
}

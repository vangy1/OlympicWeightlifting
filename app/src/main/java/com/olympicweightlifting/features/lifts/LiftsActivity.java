package com.olympicweightlifting.features.lifts;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.olympicweightlifting.App;
import com.olympicweightlifting.R;
import com.olympicweightlifting.features.lifts.LiftsPagerAdapter.LiftsPagerAdapterBuilder;
import com.olympicweightlifting.mainpage.FeaturesRecyclerViewAdapter;
import com.olympicweightlifting.mainpage.MainActivity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class LiftsActivity extends DaggerAppCompatActivity {
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.image_header)
    ImageView imageHeader;
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
        setContentView(R.layout.activity_lifts);
        ButterKnife.bind(this);
        ((App) getApplication()).getAnalyticsTracker().sendScreenName("Lifts Activity");

        setupTabLayout();

        Bundle extras = getIntent().getExtras();
        setImageHeader(extras.getInt(MainActivity.BUNDLE_LIFTS_HEADER_IMAGE));
        setupViewPager(new Gson().fromJson(extras.getString(MainActivity.BUNDLE_LIFTS_ACTIVITY_DATA), new TypeToken<List<LiftsFragmentData>>() {
        }.getType()));

    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.lifts_technique));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.lifts_mistakes));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.lifts_exercises));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }

    private void setImageHeader(int liftsHeaderImage) {
        imageHeader.setImageResource(liftsHeaderImage);
    }

    private void setupViewPager(List<LiftsFragmentData> liftsActivityData) {
        LiftsPagerAdapter liftsPagerAdapter = new LiftsPagerAdapterBuilder(getSupportFragmentManager())
                .addFragment(liftsActivityData.get(0))
                .addFragment(liftsActivityData.get(1))
                .addFragment(liftsActivityData.get(2))
                .build();
        viewPager.setAdapter(liftsPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        int fragmentIndex = getIntent().getIntExtra(FeaturesRecyclerViewAdapter.BUNDLE_FRAGMENT_INDEX, 0);
        viewPager.setCurrentItem(fragmentIndex);
    }
}

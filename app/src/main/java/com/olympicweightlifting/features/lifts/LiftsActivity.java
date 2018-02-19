package com.olympicweightlifting.features.lifts;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.olympicweightlifting.R;
import com.olympicweightlifting.features.lifts.LiftsPagerAdapter.LiftsPagerAdapterBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiftsActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.image_header)
    ImageView imageHeader;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifts);
        ButterKnife.bind(this);

        setupTabLayout();

        Bundle extras = getIntent().getExtras();
        setupViewPager(new Gson().fromJson(extras.getString(getString(R.string.lifts_activity_data)), new TypeToken<List<LiftsFragmentData>>() {
        }.getType()));

        setImageHeader(extras.getInt(getString(R.string.lifts_header_image)));
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Technique"));
        tabLayout.addTab(tabLayout.newTab().setText("Mistakes"));
        tabLayout.addTab(tabLayout.newTab().setText("Exercises"));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }

    private void setupViewPager(List<LiftsFragmentData> liftsActivityData) {
        LiftsPagerAdapter liftsPagerAdapter = new LiftsPagerAdapterBuilder(getSupportFragmentManager(), getApplicationContext())
                .addFragment(liftsActivityData.get(0))
                .addFragment(liftsActivityData.get(1))
                .addFragment(liftsActivityData.get(2))
                .build();
        viewPager.setAdapter(liftsPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        int fragmentIndex = getIntent().getIntExtra(getString(R.string.extra_fragment_index), 0);
        viewPager.setCurrentItem(fragmentIndex);
    }

    private void setImageHeader(int liftsHeaderImage) {
        imageHeader.setImageResource(liftsHeaderImage);
    }
}

package com.olympicweightlifting.calculators;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.olympicweightlifting.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalculatorsActivity extends AppCompatActivity {
    @BindView(R.id.calculators_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.calculators_view_pager)
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
        tabLayout.addTab(tabLayout.newTab().setText("Sinclair"));
        tabLayout.addTab(tabLayout.newTab().setText("Rep Max"));
        tabLayout.addTab(tabLayout.newTab().setText("Loading"));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }

    private void setupViewPager() {
        viewPager.setAdapter(new CalculatorsPagerAdapter(getFragmentManager()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}

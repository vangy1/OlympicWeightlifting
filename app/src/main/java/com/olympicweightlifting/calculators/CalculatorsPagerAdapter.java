package com.olympicweightlifting.calculators;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.olympicweightlifting.calculators.loading.LoadingCalculatorFragment;
import com.olympicweightlifting.calculators.repmax.RepmaxCalculatorFragment;
import com.olympicweightlifting.calculators.sinclair.SinclairCalculatorFragment;

public class CalculatorsPagerAdapter extends FragmentStatePagerAdapter {
    private SinclairCalculatorFragment sinclairCalculatorFragment = new SinclairCalculatorFragment();
    private RepmaxCalculatorFragment repmaxCalculatorFragment = new RepmaxCalculatorFragment();
    private LoadingCalculatorFragment loadingCalculatorFragment = new LoadingCalculatorFragment();

    public CalculatorsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return sinclairCalculatorFragment;
            case 1:
                return repmaxCalculatorFragment;
            case 2:
                return loadingCalculatorFragment;
            default:
                return sinclairCalculatorFragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}

package com.olympicweightlifting.di;


import com.olympicweightlifting.calculators.CalculatorsActivity;
import com.olympicweightlifting.calculators.sinclair.SinclairCalculatorFragment;
import com.olympicweightlifting.mainpage.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Binds all sub-components within the app.
 */
@Module
public abstract class BuildersModule {
    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();
    @ContributesAndroidInjector
    abstract CalculatorsActivity bindCalculatorActivity();
    @ContributesAndroidInjector
    abstract SinclairCalculatorFragment bindSinclairCalculatorFragment();
}

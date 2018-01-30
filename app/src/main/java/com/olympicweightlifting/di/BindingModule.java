package com.olympicweightlifting.di;


import com.olympicweightlifting.features.calculators.CalculatorsActivity;
import com.olympicweightlifting.features.calculators.sinclair.SinclairCalculatorFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BindingModule {

    @ContributesAndroidInjector
    abstract CalculatorsActivity bindCalculatorActivity();
    @ContributesAndroidInjector
    abstract SinclairCalculatorFragment bindSinclairCalculatorFragment();
}

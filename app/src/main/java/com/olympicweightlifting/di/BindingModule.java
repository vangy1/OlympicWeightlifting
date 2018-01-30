package com.olympicweightlifting.di;


import com.olympicweightlifting.calculators.CalculatorsActivity;
import com.olympicweightlifting.calculators.sinclair.SinclairCalculatorFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BindingModule {

    @ContributesAndroidInjector
    abstract CalculatorsActivity bindCalculatorActivity();
    @ContributesAndroidInjector
    abstract SinclairCalculatorFragment bindSinclairCalculatorFragment();
}
